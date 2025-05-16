package sync;

import manager.RegistryManager;

import java.io.IOException;
import java.nio.file.*;

/**
 * Maillon de la chaîne chargé d'enregistrer un fichier inconnu dans le registre.
 *
 * <p>
 * Si un fichier est détecté uniquement dans A ou B et qu’il n’est pas encore enregistré,
 * ce handler déclenche une copie vers l'autre répertoire, puis enregistre la date
 * de modification dans le registre.
 * </p>
 *
 * <p>
 * Ce maillon est généralement placé en tête de chaîne pour garantir un historique
 * avant tout autre traitement.
 *
 * @see RegistryManager
 * @see SyncContext
 * @since JDK 17
 */
public class RegisterHandler extends AbstractSyncHandler {

    @Override
    public void handle(SyncContext context) {
        try {
            boolean existsA = Files.exists(context.pathA);
            boolean existsB = Files.exists(context.pathB);
            boolean alreadyRegistered = context.registry.contains(context.relativePath);

            if (!alreadyRegistered) {
                if (existsA) {
                    registerAndCopy(context.pathA, context.pathB, "A→B", context);
                } else if (existsB) {
                    registerAndCopy(context.pathB, context.pathA, "B→A", context);
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement : " + e.getMessage());
        }

        super.handle(context);
    }

    /**
     * Copie un fichier source vers destination et l’enregistre dans le registre.
     *
     * @param source   le fichier existant (dans A ou B)
     * @param target   la destination où copier le fichier
     * @param label    label affiché dans les logs ("A→B" ou "B→A")
     * @param context  le contexte de synchronisation
     * @throws IOException si une erreur survient lors de la copie ou lecture
     */
    private void registerAndCopy(Path source, Path target, String label, SyncContext context) throws IOException {
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Enregistrement + copie " + label + " : " + context.relativePath);

        long lastModified = Files.getLastModifiedTime(source).toMillis();
        context.registry.put(context.relativePath, lastModified);
        RegistryManager.getInstance().saveRegistry(context.registry);
    }
}