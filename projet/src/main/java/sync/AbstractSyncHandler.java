package sync;

/**
 * Classe abstraite du patron <strong>Chain of Responsibility</strong>.
 *
 * <p>
 * Fournit le chaînage automatique entre les différents {@link SyncHandler}.
 * Chaque maillon peut décider de déléguer le traitement au suivant via {@link #setNext(SyncHandler)}.
 * </p>
 *
 * <p>
 * Les classes concrètes comme {@code CopyHandler}, {@code DeleteHandler}, etc., hériteront
 * de cette classe pour bénéficier du chaînage par défaut.
 *
 * @see SyncHandler
 * @see SyncContext
 * @since JDK 17
 */
public abstract class AbstractSyncHandler implements SyncHandler {
    /** Référence vers le prochain maillon de la chaîne. */
    private SyncHandler next;

    /** Tolérance en millisecondes pour éviter les faux positifs de conflit. */
    protected static final long TIME_TOLERANCE_MS = 10;

    @Override
    public void setNext(SyncHandler next) {
        this.next = next;
    }

    @Override
    public void handle(SyncContext context) {
        if (next != null) {
            next.handle(context);
        }
    }
}