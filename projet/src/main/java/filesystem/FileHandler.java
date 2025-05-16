package filesystem;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface du système de fichiers abstrait pour les opérations de synchronisation.
 *
 * <p>
 * Permet d’effectuer des opérations génériques sur des fichiers (copie, suppression, etc.)
 * sans se soucier du système sous-jacent (local, distant, WebDAV, etc.).
 * </p>
 *
 * <p>
 * Ce point d’abstraction facilite l’extension vers d’autres protocoles (HTTP, FTP...).
 *
 * @see LocalFileHandler
 * @since JDK 17
 */
public interface FileHandler {

    /**
     * Copie un fichier vers une nouvelle destination.
     *
     * @param source le chemin source
     * @param destination le chemin destination
     * @throws IOException si une erreur survient
     */
    void copy(Path source, Path destination) throws IOException;

    /**
     * Supprime un fichier.
     *
     * @param path le fichier à supprimer
     * @throws IOException si une erreur survient
     */
    void delete(Path path) throws IOException;

    /**
     * Renvoie le temps de dernière modification.
     *
     * @param path le fichier concerné
     * @return timestamp en millisecondes
     * @throws IOException si une erreur survient
     */
    long getLastModified(Path path) throws IOException;

    /**
     * Vérifie si un fichier existe.
     *
     * @param path le fichier à tester
     * @return true s’il existe
     */
    boolean exists(Path path);
}