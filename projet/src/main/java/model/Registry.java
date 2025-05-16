package model;

import java.util.*;

/**
 * Représente le registre de synchronisation associé à un profil.
 *
 * <p>
 * Le registre enregistre, pour chaque fichier synchronisé, sa dernière
 * date de modification connue (exprimée en millisecondes depuis l’époque Unix).
 * Ces données permettent à l’outil de synchronisation de détecter les
 * changements, suppressions ou conflits entre les deux répertoires.
 * </p>
 *
 * <p>
 * Les chemins utilisés sont relatifs à la racine du profil et servent de clés
 * dans la map {@code entries}. Cette classe est principalement utilisée par
 * {@link manager.RegistryManager} et {@link sync.SyncContext}.
 * </p>
 *
 * @see manager.RegistryManager
 * @see sync.SyncContext
 * @since JDK 17
 */
public class Registry {
    /** Nom du profil associé à ce registre. */
    private String profileName;

    /** Entrées du registre : chemin relatif → date de dernière modification (en millis). */
    private Map<String, Long> entries;

    /**
     * Construit un registre pour un profil donné.
     *
     * @param profileName nom du profil associé
     */
    public Registry(String profileName) {
        this.profileName = profileName;
        this.entries = new HashMap<>();
    }

    /**
     * Retourne le nom du profil associé à ce registre.
     *
     * @return le nom du profil
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Définit le nom du profil associé.
     *
     * @param profileName nom à associer
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * Retourne une vue non modifiable des entrées du registre.
     *
     * @return la map des chemins relatifs vers les timestamps (lecture seule)
     */
    public Map<String, Long> getEntries() {
        return Collections.unmodifiableMap(entries);
    }

    /**
     * Ajoute ou met à jour une entrée dans le registre.
     *
     * @param relativePath chemin relatif du fichier
     * @param lastModified date de dernière modification (en millis)
     */
    public void put(String relativePath, long lastModified) {
        entries.put(relativePath, lastModified);
    }

    /**
     * Récupère la date de dernière modification connue pour un fichier.
     *
     * @param relativePath chemin relatif
     * @return timestamp enregistré, ou {@code null} si inconnu
     */
    public Long get(String relativePath) {
        return entries.get(relativePath);
    }

    /**
     * Supprime une entrée du registre.
     *
     * @param relativePath chemin relatif à retirer
     */
    public void remove(String relativePath) {
        entries.remove(relativePath);
    }

    /**
     * Vérifie si une entrée est présente dans le registre.
     *
     * @param relativePath chemin relatif à vérifier
     * @return {@code true} si une entrée existe, sinon {@code false}
     */
    public boolean contains(String relativePath) {
        return entries.containsKey(relativePath);
    }
}