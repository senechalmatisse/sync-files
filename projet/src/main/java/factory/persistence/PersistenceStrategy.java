package factory.persistence;

import java.io.IOException;

/**
 * Interface du patron <strong>Stratégie</strong> pour la persistance des données.
 *
 * <p>
 * Permet d’abstraire les mécanismes de sérialisation/désérialisation
 * pour tout type d’objet, en fournissant une API générique.
 * </p>
 *
 * <p>
 * Grâce à cette interface, les composants comme {@code ProfileManager}
 * ou {@code RegistryManager} peuvent rester découplés de la technologie
 * de persistance (JSON, XML, binaire...).
 *
 * @param <T> le type d’objet à gérer
 *
 * @see XmlPersistenceStrategy
 * @since JDK 17
 */
public interface PersistenceStrategy<T> {

    /**
     * Sérialise et enregistre un objet dans le fichier spécifié.
     *
     * @param path  chemin absolu ou relatif vers le fichier de destination
     * @param value objet à sauvegarder
     * @throws IOException si une erreur survient à l’écriture
     */
    void save(String path, T value) throws IOException;

    /**
     * Désérialise un objet depuis un fichier donné.
     *
     * @param path chemin du fichier source
     * @param type classe attendue de l’objet désérialisé
     * @return l’objet reconstruit depuis le fichier
     * @throws IOException si une erreur survient à la lecture ou à la conversion
     */
    T load(String path, Class<T> type) throws IOException;

    /**
     * Calcule un nom de fichier valide à partir d’un nom logique (ex : nom de profil).
     *
     * <p>Cette méthode permet d’abstraire l’extension ou la logique de nommage.</p>
     *
     * @param name nom logique d’un objet
     * @return nom de fichier persistant associé
     */
    String getFileName(String name);
}