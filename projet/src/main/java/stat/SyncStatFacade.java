package stat;

import java.io.IOException;

import factory.*;
import manager.*;
import model.Profile;

/**
 * Point d'accès simplifié à l'état d’un profil de synchronisation.
 *
 * <p>
 * {@code SyncStatFacade} implémente le patron de conception <strong>Façade</strong>,
 * en exposant une méthode unique {@link #printStatus(String)} pour afficher :
 * </p>
 * <ul>
 *     <li>les informations de base du profil (nom, dossiers A et B) ;</li>
 *     <li>le contenu lisible du registre associé (via {@link RegistryManager}).</li>
 * </ul>
 *
 * <p>
 * Cette classe est statique et utilitaire : elle ne peut être instanciée.
 * Elle encapsule la logique combinée de {@link ProfileManager} et
 * {@link RegistryManager} pour faciliter l’observation rapide d’un état de synchronisation.
 *
 * @see ProfileManager
 * @see RegistryManager
 * @see model.Profile
 * @since JDK 17
 */
public final class SyncStatFacade {

    /**
     * Constructeur privé : empêche l’instanciation de cette classe utilitaire.
     */
    private SyncStatFacade() {

    }

    /**
     * Affiche dans la console les détails d’un profil de synchronisation.
     *
     * <p>
     * Cela inclut les chemins des répertoires A et B définis dans le profil,
     * ainsi que les entrées du registre affichées avec des horodatages lisibles.
     * </p>
     *
     * @param profileName le nom du profil (sans extension)
     */
    public static void printStatus(String profileName) {
        try {
            ProfileManager.init(new XmlProfileStrategyFactory().createStrategy());
            Profile profile = ProfileManager.getInstance().loadProfile(profileName);

            System.out.println("Profil : " + profile.getName());
            System.out.println("Dossier A : " + profile.getPathA());
            System.out.println("Dossier B : " + profile.getPathB());

            // Affichage du registre
            RegistryManager.init(new XmlRegistryStrategyFactory().createStrategy());
            RegistryManager.getInstance().printRegistry(profileName);
        } catch (IOException e) {
            System.err.println("Impossible de charger le profil \"" + profileName + "\" : " + e.getMessage());
        }
    }
}