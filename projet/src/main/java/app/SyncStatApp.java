package app;

import stat.SyncStatFacade;

/**
 * Point d’entrée de l’application <strong>syncstat</strong>.
 *
 * <p>
 * Ce programme est utilisé pour afficher l’état d’un profil de synchronisation :
 * </p>
 * <ul>
 *     <li>Il affiche les informations du profil (nom, chemins A et B)</li>
 *     <li>Il affiche le contenu du registre associé (fichiers synchronisés avec date de dernière synchro)</li>
 * </ul>
 *
 * <p>
 * Il s’appuie sur le patron de conception <strong>Façade</strong> pour encapsuler
 * la logique de chargement du profil et du registre dans {@link stat.SyncStatFacade}.
 * </p>
 *
 * <p>
 * Ce programme est utile pour vérifier que les opérations de lecture de profils et de registres fonctionnent
 * correctement, notamment dans le cadre de tests ou de débogage.
 *
 * @see stat.SyncStatFacade
 * @since JDK 17
 */
public class SyncStatApp {

    /**
     * Méthode principale.
     * Attend en argument le nom du profil à afficher.
     *
     * @param args un seul argument attendu : le nom du profil
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage : java -cp target/classes src.main.java.app.Client <profile-name>");
            return;
        }

        String profileName = args[0];
        SyncStatFacade.printStatus(profileName);
    }
}