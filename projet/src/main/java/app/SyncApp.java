package app;

import java.io.IOException;
import java.nio.file.*;

import factory.*;
import filesystem.*;
import manager.*;
import model.*;
import sync.*;
import visitor.FileVisitor;
import visitor.SyncVisitor;

/**
 * Point d’entrée de l’application <strong>sync</strong>.
 *
 * <p>
 * Ce programme lance la synchronisation bidirectionnelle entre deux
 * répertoires définis dans un profil préalablement enregistré.
 * Il utilise plusieurs patrons de conception :
 * </p>
 *
 * <ul>
 *     <li><strong>Composite</strong> pour parcourir l’arborescence de fichiers</li>
 *     <li><strong>Visitor</strong> pour appliquer la logique de synchronisation</li>
 *     <li><strong>Chain of Responsibility</strong> pour modulariser les traitements
 *         (copie, suppression, conflit, enregistrement)</li>
 *     <li><strong>Singleton</strong> pour l’accès centralisé au gestionnaire de profil et au registre</li>
 * </ul>
 *
 * <p>
 * L’outil garantit qu’à la fin du processus, les deux répertoires sont
 * synchronisés (même fichiers, même dates), en tenant compte de l’historique
 * dans un fichier de registre.
 *
 * @see manager.ProfileManager
 * @see manager.RegistryManager
 * @see model.FileComponent
 * @see visitor.FileVisitor
 * @see sync.SyncHandler
 * @since JDK 17
 */
public class SyncApp {

    /**
     * Lance le programme de synchronisation à partir d’un nom de profil.
     *
     * @param args un seul argument attendu : le nom du profil enregistré
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -cp target/classes src.main.java.app.SyncApp <profile-name>");
            return;
        }

        String profileName = args[0];

        try {
            // 1. Charger le profil
            ProfileManager.init(new XmlProfileStrategyFactory().createStrategy());
            RegistryManager.init(new XmlRegistryStrategyFactory().createStrategy());

            Profile profile = ProfileManager.getInstance().loadProfile(profileName);
            Path pathA = Paths.get(profile.getPathA());
            Path pathB = Paths.get(profile.getPathB());

            System.out.println("Synchronisation du profil : " + profile.getName());
            System.out.println("Dossier A : " + pathA);
            System.out.println("Dossier B : " + pathB);

            // 2. Explorer les répertoires (Composite)
            FileComponent arbreA = FileSystemExplorer.explore(pathA);
            FileComponent arbreB = FileSystemExplorer.explore(pathB);

            // 3. Charger le registre
            Registry registry = RegistryManager.getInstance().loadRegistry(profileName);

            // 4.  Créer les chaînes de traitement
            SyncHandler handlerAtoB = createHandlerChain();
            SyncHandler handlerBtoA = createHandlerChain();

            // 5. Créer le FileHandler
            FileHandler fileHandler = FileHandlerFactory.createLocalFileHandler();

            // 6. Créer les visiteurs pour chaque sens
            FileVisitor syncAtoB = new SyncVisitor(pathA, pathB, handlerAtoB, fileHandler, registry);
            FileVisitor syncBtoA = new SyncVisitor(pathB, pathA, handlerBtoA, fileHandler, registry);

            // 7. Visiter les deux arborescences
            arbreA.accept(syncAtoB);
            arbreB.accept(syncBtoA);

            // 8. Sauvegarder le registre mis à jour
            RegistryManager.getInstance().saveRegistry(registry);
            System.out.println("Synchronisation bidirectionnelle terminée.");
        } catch (IOException e) {
            System.err.println("Erreur d'E/S : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Erreur : " + e.getMessage());
        }
    }

    /**
     * Crée et retourne une chaîne complète de gestion de synchronisation :
     * enregistrement ➜ copie ➜ suppression ➜ gestion des conflits.
     *
     * @return la tête de la chaîne de synchronisation
     */
    private static SyncHandler createHandlerChain() {
        SyncHandler register = new RegisterHandler();
        SyncHandler copy = new CopyHandler();
        SyncHandler delete = new DeleteHandler();
        SyncHandler conflict = new ConflictHandler();

        register.setNext(copy);
        copy.setNext(delete);
        delete.setNext(conflict);

        return register;
    }
}