/**
 * Contient les composants centraux de la logique de synchronisation de fichiers,
 * structurés autour du patron de conception <strong>Chaîne de Responsabilité</strong>.
 *
 * <p>
 * Le package {@code sync} définit une chaîne de traitement modulaire, dans laquelle
 * chaque {@link sync.SyncHandler} est responsable d'une opération spécifique de synchronisation :
 * enregistrement, copie, suppression ou résolution de conflit.
 * </p>
 *
 * <p>
 * Cette architecture permet une extension flexible : de nouveaux maillons peuvent
 * être ajoutés à la chaîne sans modifier les classes existantes, en accord avec
 * le principe ouvert/fermé (OCP) du SOLID.
 * </p>
 *
 * <h2>Composants clés</h2>
 * <ul>
 *     <li>{@link sync.SyncHandler} : interface commune à tous les maillons de la chaîne.</li>
 *     <li>{@link sync.AbstractSyncHandler} : classe abstraite facilitant l’enchaînement des maillons.</li>
 *     <li>{@link sync.RegisterHandler} : enregistrement initial des fichiers inconnus.</li>
 *     <li>{@link sync.CopyHandler} : copie de fichiers plus récents d’un côté vers l’autre.</li>
 *     <li>{@link sync.DeleteHandler} : suppression des fichiers disparus.</li>
 *     <li>{@link sync.ConflictHandler} : gestion interactive des conflits de modification.</li>
 *     <li>{@link sync.SyncContext} : encapsule toutes les données nécessaires à un traitement unitaire.</li>
 * </ul>
 *
 * <p>
 * Ce package est utilisé par le visiteur {@code SyncVisitor} du package {@code visitor},
 * lequel applique cette chaîne à chaque fichier à synchroniser.
 * </p>
 *
 * @see sync.SyncHandler
 * @see sync.AbstractSyncHandler
 * @see sync.SyncContext
 * @since JDK 17
 */
package sync;