/**
 * Fournit les composants liés au patron de conception <strong>Visiteur</strong>
 * appliqué à une structure de fichiers modélisée par le patron <strong>Composite</strong>.
 *
 * <p>
 * Le package {@code visitor} définit une interface {@link visitor.FileVisitor} qui
 * permet de visiter des structures hiérarchiques composées de fichiers et de répertoires,
 * à travers les classes {@link model.FileLeaf} et {@link model.DirectoryComposite}.
 * </p>
 *
 * <p>
 * Cette séparation entre la structure (dans le package {@code model}) et le comportement
 * permet une extensibilité forte sans modifier les classes du modèle. On peut ainsi ajouter
 * de nouveaux traitements (affichage, synchronisation, comptage...) en définissant
 * de nouveaux visiteurs.
 * </p>
 *
 * <p>
 * Le visiteur {@link visitor.SyncVisitor} est une implémentation concrète qui déclenche
 * une synchronisation bidirectionnelle de fichiers entre deux répertoires selon une logique
 * de traitement déléguée à une chaîne de responsabilité.
 * </p>
 *
 * @see visitor.FileVisitor
 * @see visitor.SyncVisitor
 * @see model.FileComponent
 * @see model.DirectoryComposite
 * @see model.FileLeaf
 * @since JDK 17
 */
package visitor;