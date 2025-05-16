/**
 * Contient les classes représentant les entités métiers et les structures de données
 * utilisées dans le processus de synchronisation de fichiers.
 *
 * <p>
 * Le package {@code model} implémente principalement deux motifs de conception :
 * </p>
 *
 * <ul>
 *     <li><strong>Composite</strong> : pour modéliser une arborescence de fichiers avec
 *         {@link model.FileComponent}, {@link model.FileLeaf}, et {@link model.DirectoryComposite} ;</li>
 *     <li><strong>Immuabilité</strong> : via la classe {@link model.Profile}, qui décrit un profil
 *         de synchronisation comme une entité stable et identifiée par un nom logique.</li>
 * </ul>
 *
 * <p>
 * Le modèle inclut également :
 * </p>
 * <ul>
 *     <li>{@link model.Registry} : une structure clé-valeur pour suivre l’historique des fichiers synchronisés,</li>
 *     <li>{@link model.FileSystemExplorer} : une classe utilitaire pour construire dynamiquement la structure Composite
 *         à partir du système de fichiers local.</li>
 * </ul>
 *
 * <p>
 * Ces classes sont étroitement liées aux composants de persistance, de gestion, et au traitement par visiteur,
 * formant ainsi la base de l’architecture logicielle du système.
 * </p>
 *
 * @see model.Profile
 * @see model.Registry
 * @see model.FileComponent
 * @since JDK 17
 */
package model;