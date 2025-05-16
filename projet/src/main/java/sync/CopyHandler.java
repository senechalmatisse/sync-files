package sync;

import manager.RegistryManager;

import java.io.IOException;
import java.nio.file.*;

/**
 * Maillon de la chaîne chargé de copier un fichier plus récent d’un côté vers l’autre.
 *
 * <p>
 * Compare les dates de modification des fichiers dans A et B.
 * Si l’un est plus récent et que la différence dépasse le seuil de tolérance,
 * il est copié vers l’autre répertoire. Le registre est alors mis à jour
 * avec la date du fichier source.
 *
 * @see SyncContext
 * @see RegistryManager
 * @since JDK 17
 */
public class CopyHandler extends AbstractSyncHandler {

    @Override
    public void handle(SyncContext context) {
        try {
            if (!Files.exists(context.pathA) || !Files.exists(context.pathB)) {
                super.handle(context); // L’un des fichiers est absent : rien à faire ici
                return;
            }

            long timeA = getLastModified(context.pathA);
            long timeB = getLastModified(context.pathB);

            if (Math.abs(timeA - timeB) <= TIME_TOLERANCE_MS) {
                super.handle(context); // Différence négligeable
                return;
            }

            if (timeA > timeB) {
                copyAndRegister(context.pathA, context.pathB, timeA, "A → B", context);
            } else {
                copyAndRegister(context.pathB, context.pathA, timeB, "B → A", context);
            }

            RegistryManager.getInstance().saveRegistry(context.registry);
        } catch (IOException e) {
            System.err.println("Erreur de copie : " + e.getMessage());
        }

        super.handle(context);
    }

    /**
     * Récupère le temps de dernière modification d’un fichier.
     *
     * @param path chemin du fichier
     * @return timestamp en millisecondes
     * @throws IOException si erreur d'accès au fichier
     */
    private long getLastModified(Path path) throws IOException {
        return Files.getLastModifiedTime(path).toMillis();
    }

    /**
     * Copie un fichier source vers sa destination et met à jour le registre.
     *
     * @param source   chemin source
     * @param target   chemin de destination
     * @param time     timestamp du fichier source
     * @param label    description pour affichage
     * @param context  contexte de synchronisation
     * @throws IOException si erreur de copie ou de mise à jour du registre
     */
    private void copyAndRegister(Path source, Path target, long time, String label, SyncContext context) throws IOException {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        context.registry.put(context.relativePath, time);
        System.out.println("Copie " + label + " : " + source.getFileName());
    }
}