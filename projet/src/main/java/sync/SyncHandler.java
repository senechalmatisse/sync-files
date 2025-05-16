package sync;

/**
 * Interface du patron de conception <strong>Chain of Responsibility</strong>
 * pour le traitement des fichiers lors d’une synchronisation.
 *
 * <p>
 * Chaque implémentation (copie, suppression, conflit, enregistrement...) représente un maillon
 * de la chaîne et peut soit traiter un {@link SyncContext}, soit déléguer au maillon suivant.
 *
 * @see SyncContext
 * @see AbstractSyncHandler
 * @since JDK 17
 */
public interface SyncHandler {

    /**
     * Définit le maillon suivant dans la chaîne de synchronisation.
     *
     * @param next le {@code SyncHandler} à appeler si ce maillon ne traite pas le contexte
     */
    void setNext(SyncHandler next);

    /**
     * Tente de traiter le fichier selon une règle spécifique.
     * Si ce maillon ne peut pas gérer le contexte, il doit appeler
     * {@code next.handle(context)}.
     *
     * @param context le contexte courant de synchronisation (informations sur les fichiers, registre, etc.)
     */
    void handle(SyncContext context);
}