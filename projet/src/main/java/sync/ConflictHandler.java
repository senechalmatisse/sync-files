package sync;

import manager.RegistryManager;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.util.Scanner;

/**
 * Maillon de la chaîne de synchronisation chargé de résoudre les conflits de modification entre deux fichiers.
 *
 * <p>
 * Implémente le patron <strong>Chain of Responsibility</strong> via {@link AbstractSyncHandler}.
 * Ce maillon est appelé lorsqu'un même fichier existe dans les deux répertoires A et B,
 * mais que leurs dates de modification diffèrent significativement.
 * </p>
 *
 * <p>
 * L'utilisateur est invité à choisir la version à conserver. La version sélectionnée est copiée
 * vers l’autre répertoire et le registre est mis à jour avec sa date de modification.
 *
 * @see SyncHandler
 * @see AbstractSyncHandler
 * @see SyncContext
 * @see manager.RegistryManager
 * @since JDK 17
 */
public class ConflictHandler extends AbstractSyncHandler {
    /** Scanner global pour lire la décision de l'utilisateur en cas de conflit. */
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public void handle(SyncContext context) {
        try {
            if (!Files.exists(context.pathA) || !Files.exists(context.pathB)) {
                super.handle(context);
                return;
            }

            long timeA = Files.getLastModifiedTime(context.pathA).toMillis();
            long timeB = Files.getLastModifiedTime(context.pathB).toMillis();
            long diff = Math.abs(timeA - timeB);

            if (diff <= TIME_TOLERANCE_MS) {
                super.handle(context);
                return;
            }

            promptUserForResolution(context, timeA, timeB);
            RegistryManager.getInstance().saveRegistry(context.registry);
        } catch (IOException e) {
            System.err.println("Erreur de résolution de conflit : " + e.getMessage());
        }

        super.handle(context);
    }

    /**
     * Affiche les détails du conflit et traite le choix utilisateur.
     *
     * @param context contexte de synchronisation contenant les chemins et le registre
     * @param timeA timestamp du fichier A
     * @param timeB timestamp du fichier B
     * @throws IOException si une erreur survient lors de la copie ou de la mise à jour du registre
     */
    private void promptUserForResolution(SyncContext context, long timeA, long timeB) throws IOException {
        System.out.println("Conflit détecté : " + context.pathA.getFileName());
        System.out.printf("A : %s | B : %s%n", formatTime(timeA), formatTime(timeB));
        System.out.print("Choisir le sens de copie ([A]>B / B>[A]) : ");

        String input = scanner.nextLine().trim().toLowerCase();

        switch (input.isEmpty() ? "?" : input.substring(0, 1)) {
            case "a" -> resolveConflict(context.pathA, context.pathB, timeA, "A vers B", context);
            case "b" -> resolveConflict(context.pathB, context.pathA, timeB, "B vers A", context);
            default -> System.out.println("Conflit ignoré.");
        }
    }

    /**
     * Copie un fichier d'une source vers une destination et met à jour le registre.
     *
     * @param source chemin source à copier
     * @param target chemin de destination
     * @param timestamp date de dernière modification du fichier source
     * @param label description textuelle (pour affichage)
     * @param context contexte de synchronisation courant
     * @throws IOException en cas d’échec de copie ou d’accès au registre
     */
    private void resolveConflict(Path source, Path target, long timestamp, String label, SyncContext context) throws IOException {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        context.registry.put(context.relativePath, timestamp);
        System.out.println("Conflit résolu par copie de " + label + ".");
    }

    /**
     * Convertit un timestamp en millisecondes en représentation lisible.
     *
     * @param millis temps exprimé en millisecondes depuis l'époque Unix
     * @return chaîne de caractères représentant la date lisible
     */
    private String formatTime(long millis) {
        return FileTime.fromMillis(millis).toString();
    }
}