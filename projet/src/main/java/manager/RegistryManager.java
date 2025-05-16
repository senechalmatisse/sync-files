package manager;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import factory.persistence.*;
import model.Registry;

/**
 * Gestionnaire central des registres de synchronisation.
 *
 * <p>
 * Applique le patron <strong>Singleton</strong> pour garantir une seule instance.
 * Les registres enregistrent les horodatages des fichiers synchronisés,
 * permettant de détecter les modifications, suppressions ou conflits.
 * </p>
 *
 * <p>
 * Les opérations de persistance sont déléguées à une {@link PersistenceStrategy},
 * ce qui rend le système flexible (JSON, XML, binaire...).
 * </p>
 *
 * <p>
 * Les registres sont stockés dans le dossier {@code registries/} avec le nom
 * {@code registry_<profile>.xml} (ou un autre format selon la stratégie).
 *
 * @see Registry
 * @see PersistenceStrategy
 * @since JDK 17
 */
public class RegistryManager {
    /** Instance unique du singleton. */
    private static RegistryManager instance = null;

    /** Verrou de synchronisation. */
    private static final Object lock = RegistryManager.class;

    /** Dossier où sont stockés les registres. */
    private static final String REGISTRY_DIRECTORY = "registries";

    /** Stratégie de persistance utilisée pour les registres. */
    private final PersistenceStrategy<Registry> strategy;

    /**
     * Constructeur privé qui initialise le répertoire de registre.
     *
     * @param strategy stratégie de sérialisation utilisée
     */
    private RegistryManager(PersistenceStrategy<Registry> strategy) {
        this.strategy = strategy;
        try {
            Files.createDirectories(Paths.get(REGISTRY_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException("Impossible de créer le dossier des registres.", e);
        }
    }

    /**
     * Initialise le singleton avec une stratégie personnalisée.
     *
     * @param strategy stratégie de persistance à utiliser
     */
    public static void init(PersistenceStrategy<Registry> strategy) {
        synchronized (lock) {
            if (instance == null) {
                instance = new RegistryManager(strategy);
            } else {
                throw new IllegalStateException("RegistryManager est déjà initialisé.");
            }
        }
    }

    /**
     * Retourne l’instance unique du gestionnaire.
     *
     * @return instance unique de {@code RegistryManager}
     * @throws IllegalStateException si {@link #init(PersistenceStrategy)} n'a jamais été appelé
     */
    public static RegistryManager getInstance() {
        synchronized (lock) {
            if (instance == null) {
                throw new IllegalStateException("RegistryManager n’a pas été initialisé via init(...).");
            }
            return instance;
        }
    }

    /**
     * Charge le registre associé à un profil.
     *
     * @param profileName nom du profil
     * @return le registre existant ou un registre vide
     * @throws IOException si erreur de lecture du fichier
     */
    public Registry loadRegistry(String profileName) throws IOException {
        Path path = getRegistryPath(profileName);
        if (!Files.exists(path)) {
            return new Registry(profileName);
        }
        return strategy.load(path.toString(), Registry.class);
    }

    /**
     * Sauvegarde un registre sur le disque.
     *
     * @param registry registre à enregistrer
     * @throws IOException si erreur d’écriture
     */
    public void saveRegistry(Registry registry) throws IOException {
        strategy.save(getRegistryPath(registry.getProfileName()).toString(), registry);
    }

    /**
     * Affiche les entrées d’un registre dans la console, avec horodatages lisibles.
     *
     * @param profileName nom du profil dont le registre doit être affiché
     */
    public void printRegistry(String profileName) {
        try {
            Registry reg = loadRegistry(profileName);
            System.out.println("Contenu du registre :");

            if (reg.getEntries().isEmpty()) {
                System.out.println("  (aucune entrée)");
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                for (Map.Entry<String, Long> entry : reg.getEntries().entrySet()) {
                    LocalDateTime date = Instant.ofEpochMilli(entry.getValue())
                            .atZone(ZoneId.systemDefault()).toLocalDateTime();
                    System.out.printf("  %s → %s%n", entry.getKey(), formatter.format(date));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
        }
    }

    /**
     * Construit le chemin absolu vers le fichier de registre correspondant au profil donné.
     *
     * @param profileName nom du profil
     * @return chemin du fichier de registre
     */
    private Path getRegistryPath(String profileName) {
        String fileName = strategy.getFileName("registry_" + profileName);
        return Paths.get(REGISTRY_DIRECTORY, fileName);
    }    
}