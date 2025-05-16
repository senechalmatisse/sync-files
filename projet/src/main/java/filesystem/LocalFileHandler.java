package filesystem;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;

/**
 * Implémentation locale de l’interface {@link FileHandler} utilisant l’API {@code java.nio.file}.
 *
 * <p>
 * Cette classe fournit un accès direct au système de fichiers local, en s’appuyant sur
 * les primitives de NIO pour copier, supprimer, vérifier ou dater les fichiers.
 * </p>
 *
 * <p>
 * Elle constitue la première brique concrète de la couche d’abstraction fichier,
 * et peut être remplacée par des variantes distantes (ex : WebDAV) si besoin.
 *
 * @see FileHandler
 * @since JDK 17
 */
public class LocalFileHandler implements FileHandler {

    @Override
    public void copy(Path source, Path destination) throws IOException {
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
    }

    @Override
    public void delete(Path path) throws IOException {
        Files.deleteIfExists(path);
    }

    @Override
    public long getLastModified(Path path) throws IOException {
        FileTime time = Files.getLastModifiedTime(path);
        return time.toMillis();
    }

    @Override
    public boolean exists(Path path) {
        return Files.exists(path);
    }
}