package manager;

import java.io.*;
import java.nio.file.*;

import factory.persistence.*;
import model.Profile;

/**
 * Gestionnaire central des profils de synchronisation.
 *
 * <p>
 * Cette classe applique le patron <strong>Singleton</strong> pour fournir
 * un point d’accès unique à la persistance des profils.
 * </p>
 *
 * <p>
 * Elle délègue les opérations de lecture/écriture à une stratégie de persistance
 * {@link PersistenceStrategy}, ce qui permet de changer facilement de format
 * (XML, JSON, binaire...) sans modifier cette classe.
 * </p>
 *
 * <p>
 * Les profils sont enregistrés dans le dossier {@code profiles/} avec l’extension {@code .xml}.
 *
 * @see Profile
 * @see PersistenceStrategy
 * @since JDK 17
 */
public final class ProfileManager {
    /** Instance unique du singleton. */
    private static ProfileManager instance = null;

    /** Verrou de synchronisation. */
    private static final Object lock = ProfileManager.class;

    /** Répertoire de stockage des profils. */
    private static final String PROFILE_DIRECTORY = "profiles";

    /** Stratégie de persistance utilisée pour les profils. */
    private final PersistenceStrategy<Profile> strategy;

    /**
     * Constructeur privé pour initialiser le dossier de profils.
     *
     * @param strategy stratégie de persistance utilisée
     */
    private ProfileManager(PersistenceStrategy<Profile> strategy) {
        this.strategy = strategy;
        try {
            Files.createDirectories(Paths.get(PROFILE_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier des profils.", e);
        }
    }

    /**
     * Initialise le singleton avec une stratégie spécifique.
     * À appeler **une seule fois** avant getInstance().
     *
     * @param strategy la stratégie de persistance à utiliser
     */
    public static void init(PersistenceStrategy<Profile> strategy) {
        synchronized (lock) {
            if (instance != null) {
                throw new IllegalStateException("ProfileManager a déjà été initialisé.");
            }
            instance = new ProfileManager(strategy);
        }
    }

    /**
     * Retourne l’instance unique du gestionnaire.
     *
     * @return instance unique de {@code ProfileManager}
     * @throws IllegalStateException si {@link #init(PersistenceStrategy)} n'a jamais été appelé
     */
    public static ProfileManager getInstance() {
        synchronized (lock) {
            if (instance == null) {
                throw new IllegalStateException("ProfileManager non initialisé. Appeler init() d'abord.");
            }
            return instance;
        }
    }

    /**
     * Sauvegarde un profil dans le répertoire {@code profiles/}.
     *
     * @param profile le profil à sauvegarder
     * @throws IOException en cas d’erreur d’écriture
     */
    public void saveProfile(Profile profile) throws IOException {
        String path = PROFILE_DIRECTORY + "/" + strategy.getFileName(profile.getName());
        strategy.save(path, profile);
    }

    /**
     * Charge un profil existant à partir de son nom logique.
     *
     * @param profileName nom du profil (sans extension)
     * @return le profil correspondant
     * @throws IOException si le fichier est manquant ou corrompu
     */
    public Profile loadProfile(String profileName) throws IOException {
        String path = PROFILE_DIRECTORY + "/" + strategy.getFileName(profileName);
        if (!Files.exists(Paths.get(path))) {
            throw new IOException("Profil \"" + profileName + "\" introuvable.");
        }
        return strategy.load(path, Profile.class);
    }
}