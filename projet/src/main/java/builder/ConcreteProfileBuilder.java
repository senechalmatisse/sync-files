package builder;

import model.Profile;

/**
 * Implémentation standard du patron <strong>Builder</strong> pour les profils de synchronisation.
 *
 * <p>
 * Cette classe fournit une construction étape par étape d’un objet {@link Profile},
 * en validant les données fournies (nom, chemin A, chemin B) avant la création.
 * </p>
 *
 * <p>
 * Elle garantit que le profil construit est toujours dans un état cohérent
 * et prêt à être sauvegardé via le {@link manager.ProfileManager}.
 *
 * @see ProfileBuilder
 * @see model.Profile
 * @since JDK 17
 */

public class ConcreteProfileBuilder implements ProfileBuilder {
    /** Nom du profil. */
    private String name;

    /** Chemin du répertoire A à synchroniser. */
    private String pathA;

    /** Chemin du répertoire B à synchroniser. */
    private String pathB;

    @Override
    public void startProfile() {
        name = null;
        pathA = null;
        pathB = null;
    }

    @Override
    public void setName(String name) {
        if (this.name != null)
            throw new IllegalStateException("Profile name already set.");
        this.name = name;
    }

    @Override
    public void setPathA(String pathA) {
        if (this.pathA != null)
            throw new IllegalStateException("Path A already set.");
        this.pathA = pathA;
    }

    @Override
    public void setPathB(String pathB) {
        if (this.pathB != null)
            throw new IllegalStateException("Path B already set.");
        this.pathB = pathB;
    }

    @Override
    public Profile getProfile() {
        if (name == null || pathA == null || pathB == null)
            throw new IllegalStateException("Missing profile information.");
        return new Profile(name, pathA, pathB);
    }
}