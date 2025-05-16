package filesystem;

/**
 * Fabrique d’objets {@link FileHandler} selon le type de système de fichiers utilisé.
 *
 * <p>
 * Implémente le patron de conception <strong>Factory Method</strong> pour isoler
 * la logique de création des gestionnaires de fichiers. Actuellement limitée au système local,
 * elle peut facilement évoluer pour prendre en charge d'autres types (WebDAV, cloud, etc.).
 *
 * @see FileHandler
 * @see LocalFileHandler
 * @since JDK 17
 */
public class FileHandlerFactory {

    /**
     * Renvoie une instance de {@link FileHandler} pour le type local.
     * <p>
     * Plus tard, ce code pourra analyser un URI ou un protocole.
     * </p>
     *
     * @return une instance locale de FileHandler
     */
    public static FileHandler createLocalFileHandler() {
        return new LocalFileHandler();
    }
}