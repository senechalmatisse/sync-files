package builder;

import model.Profile;

/**
 * Interface du patron de conception <strong>Builder</strong> pour la construction d’un {@link Profile}.
 *
 * <p>
 * Définit les étapes nécessaires à la création d’un objet complexe représentant
 * un profil de synchronisation (nom, chemins A et B).
 * </p>
 *
 * <p>
 * Ce pattern permet de séparer l’algorithme de construction de l’objet final,
 * en facilitant l’extension, la validation et la personnalisation du processus.
 *
 * @see ConcreteProfileBuilder
 * @see model.Profile
 * @since JDK 17
 */
public interface ProfileBuilder {

    /**
     * Initialise ou réinitialise l'état du constructeur.
     */
    void startProfile();

    /**
     * Définit le nom du profil à construire.
     *
     * @param name le nom du profil
     */
    void setName(String name);

    /**
     * Définit le chemin du répertoire A.
     *
     * @param pathA le chemin absolu du dossier A
     */
    void setPathA(String pathA);

    /**
     * Définit le chemin du répertoire B.
     *
     * @param pathB le chemin absolu du dossier B
     */
    void setPathB(String pathB);

    /**
     * Construit et retourne un objet {@link Profile} complet.
     *
     * @return un objet Profile prêt à l'emploi
     * @throws IllegalStateException si des données sont manquantes
     */
    Profile getProfile();
}