package model;

/**
 * Représente un profil de synchronisation.
 *
 * <p>
 * Un profil définit une configuration nommée permettant de synchroniser deux
 * répertoires distincts du système de fichiers. Il est constitué de :
 * </p>
 * <ul>
 *     <li>un nom unique (identifiant logique du profil) ;</li>
 *     <li>un chemin vers un répertoire A ;</li>
 *     <li>un chemin vers un répertoire B.</li>
 * </ul>
 *
 * <p>
 * Cette classe est immuable : ses attributs sont déclarés {@code final}
 * et ne peuvent être modifiés après l’instanciation.
 * </p>
 *
 * @see manager.ProfileManager
 * @since JDK 17
 */
public class Profile {
    /** Le nom unique du profil, utilisé comme identifiant. */
    private final String name;

    /** Le chemin absolu vers le répertoire A à synchroniser. */
    private final String pathA;

    /** Le chemin absolu vers le répertoire B à synchroniser. */
    private final String pathB;

    /**
     * Construit un nouveau profil de synchronisation.
     *
     * @param name  nom logique du profil (non nul)
     * @param pathA chemin du répertoire A (non nul)
     * @param pathB chemin du répertoire B (non nul)
     * @throws IllegalArgumentException si l’un des paramètres est {@code null}
     */
    public Profile(String name, String pathA, String pathB) {
        if (name == null || pathA == null || pathB == null)
            throw new IllegalArgumentException("Les arguments du profil ne sont pas valides");
        this.name = name;
        this.pathA = pathA;
        this.pathB = pathB;
    }

    /**
     * Retourne le nom du profil.
     *
     * @return le nom du profil
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne le chemin du répertoire A.
     *
     * @return le chemin absolu du répertoire A
     */
    public String getPathA() {
        return pathA;
    }

    /**
     * Retourne le chemin du répertoire B.
     *
     * @return le chemin absolu du répertoire B
     */
    public String getPathB() {
        return pathB;
    }
}