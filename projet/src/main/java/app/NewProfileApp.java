package app;

import builder.*;
import factory.XmlProfileStrategyFactory;
import manager.ProfileManager;
import model.Profile;

/**
 * Point d’entrée de l’application <strong>new-profile</strong>.
 *
 * <p>
 * Ce programme initialise un nouveau profil de synchronisation à partir
 * d'un nom de profil et de deux chemins de répertoires (A et B).
 * Il utilise :
 * </p>
 * <ul>
 *     <li>le patron <strong>Builder</strong> pour construire un {@link Profile}</li>
 *     <li>le patron <strong>Singleton</strong> via {@link ProfileManager} pour la persistance</li>
 * </ul>
 *
 * <p>
 * Le profil est sauvegardé dans le dossier {@code profiles/} au format JSON
 * avec l’extension {@code .xml}.
 * 
 * @see builder.ProfileBuilder
 * @see builder.ConcreteProfileBuilder
 * @see builder.ProfileDirector
 * @see manager.ProfileManager
 * @see model.Profile
 * @since JDK 17
 */
public class NewProfileApp {

    /**
     * Méthode principale qui initialise et sauvegarde un profil.
     *
     * @param args trois arguments attendus :
     *  <ol>
     *      <li>Nom du profil (ex : "monProfil")</li>
     *      <li>Chemin vers le dossier A</li>
     *      <li>Chemin vers le dossier B</li>
     *  </ol>
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java -cp target/classes app.NewProfileApp <profile-name> <path-A> <path-B>");
            return;
        }

        String profileName = args[0];
        String pathA = args[1];
        String pathB = args[2];

        try {
            // Construction du profil avec le builder
            ProfileBuilder builder = new ConcreteProfileBuilder();
            ProfileDirector director = new ProfileDirector(builder);
            Profile profile = director.construct(profileName, pathA, pathB);

            // Sauvegarde avec le ProfileManager (Singleton)
            ProfileManager.init(new XmlProfileStrategyFactory().createStrategy());
            ProfileManager.getInstance().saveProfile(profile);

            System.out.println("Profil créé avec succès : " + profileName);
        } catch (Exception e) {
            System.err.println("Erreur lors de la création du profil : " + e.getMessage());
        }
    }
}