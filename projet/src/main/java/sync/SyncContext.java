package sync;

import java.nio.file.Path;

import model.Registry;

/**
 * Contexte partagé entre les maillons de la chaîne de synchronisation.
 *
 * <p>
 * Regroupe toutes les informations nécessaires au traitement d’un fichier :
 * </p>
 * <ul>
 *     <li>Chemin absolu du fichier dans le dossier A</li>
 *     <li>Chemin absolu du fichier dans le dossier B</li>
 *     <li>Chemin relatif (clé du registre)</li>
 *     <li>Registre de synchronisation associé au profil</li>
 * </ul>
 *
 * <p>
 * Ce contexte est transmis à chaque {@link SyncHandler} pour prendre
 * des décisions cohérentes sur les actions à réaliser.
 *
 * @see model.Registry
 * @since JDK 17
 */
public class SyncContext {
    /** Chemin absolu du fichier dans le dossier A. */
    public final Path pathA;

    /** Chemin absolu du fichier dans le dossier B. */
    public final Path pathB;

    /** Chemin relatif utilisé comme clé dans le registre. */
    public final String relativePath;

    /** Registre des synchronisations précédentes pour le profil courant. */
    public final Registry registry;

    /**
     * Crée un contexte de synchronisation pour un fichier donné.
     *
     * @param pathA         chemin absolu du fichier dans A
     * @param pathB         chemin absolu du fichier dans B
     * @param relativePath  chemin relatif à la racine du profil (clé du registre)
     * @param registry      registre de synchronisation courant
     */
    public SyncContext(Path pathA, Path pathB, String relativePath, Registry registry) {
        this.pathA = pathA;
        this.pathB = pathB;
        this.relativePath = relativePath;
        this.registry = registry;
    }
}