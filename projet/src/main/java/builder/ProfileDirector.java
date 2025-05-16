package builder;

import model.Profile;

/**
 * Implémentation du rôle <strong>Director</strong> dans le patron de conception {@code Builder}.
 *
 * <p>
 * Cette classe orchestre les différentes étapes de construction d’un {@link Profile}
 * en s’appuyant sur une instance de {@link ProfileBuilder}. Elle définit la séquence
 * précise d’appels à effectuer sur le builder, garantissant une création
 * cohérente et complète de l’objet final.
 * </p>
 *
 * <p>
 * Le client interagit uniquement avec ce {@code Director}, sans connaître les détails
 * de l’implémentation du builder ou de la structure interne du {@link Profile}.
 * Ce découplage améliore la maintenabilité du code et respecte les principes SOLID.
 *
 * @see ProfileBuilder
 * @see ConcreteProfileBuilder
 * @see model.Profile
 * @since JDK 17
 */
public class ProfileDirector {
    /** Instance du builder utilisé pour construire un profil. */
    private final ProfileBuilder builder;

    /**
     * Construit un directeur avec un builder spécifique.
     *
     * @param builder instance concrète de ProfileBuilder
     */
    public ProfileDirector(ProfileBuilder builder) {
        this.builder = builder;
    }

    /**
     * Lance la séquence complète de construction d’un objet {@link Profile}.
     *
     * <p>
     * Cette méthode appelle dans l’ordre les étapes de construction prévues
     * (initialisation, nom, chemin A, chemin B), puis retourne le profil final.
     *
     * @param name  nom logique du profil
     * @param pathA chemin absolu du répertoire A
     * @param pathB chemin absolu du répertoire B
     * @return un profil de synchronisation entièrement construit
     * @throws IllegalStateException si l’une des étapes échoue
     */
    public Profile construct(String name, String pathA, String pathB) {
        builder.startProfile();
        builder.setName(name);
        builder.setPathA(pathA);
        builder.setPathB(pathB);
        return builder.getProfile();
    }
}