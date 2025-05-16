package sync;

import manager.RegistryManager;

import java.io.IOException;
import java.nio.file.*;

/**
 * Maillon de la chaîne chargé de supprimer un fichier s’il a été supprimé d’un des deux côtés.
 *
 * <p>
 * Le fichier n’est supprimé que s’il est présent dans le registre, garantissant
 * qu’il ne s’agit pas d’un nouveau fichier inconnu.
 * </p>
 *
 * <p>
 * Après suppression, le fichier est également retiré du registre.
 *
 * @see RegistryManager
 * @see SyncContext
 * @since JDK 17
 */
public class DeleteHandler extends AbstractSyncHandler {

    @Override
    public void handle(SyncContext context) {
        try {
            boolean existsA = Files.exists(context.pathA);
            boolean existsB = Files.exists(context.pathB);

            // Ne traite que les fichiers connus dans le registre
            if (!context.registry.contains(context.relativePath)) {
                System.out.println("ℹIgnoré : fichier non enregistré dans le registre → " + context.relativePath);
                super.handle(context);
                return;
            }

            if (!existsA && existsB) {
                deleteFile(context.pathB, "B", context);
            } else if (existsA && !existsB) {
                deleteFile(context.pathA, "A", context);
            }
        } catch (IOException e) {
            System.err.println("Erreur de suppression : " + e.getMessage());
        }

        super.handle(context);
    }

    /**
     * Supprime un fichier et met à jour le registre.
     *
     * @param path     le chemin du fichier à supprimer
     * @param label    "A" ou "B" pour affichage
     * @param context  le contexte de synchronisation
     * @throws IOException en cas de problème lors de la suppression
     */
    private void deleteFile(Path path, String label, SyncContext context) throws IOException {
        Files.delete(path);
        System.out.println("Suppression dans " + label + " : " + path.getFileName());

        context.registry.remove(context.relativePath);
        RegistryManager.getInstance().saveRegistry(context.registry);
    }
}