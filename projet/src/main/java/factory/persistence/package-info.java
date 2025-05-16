/**
 * Fournit les interfaces et implémentations liées à la persistance des objets métiers.
 *
 * <p>
 * Le package {@code persistence} repose sur le patron de conception <strong>Stratégie</strong>
 * pour permettre une sérialisation flexible et découplée des objets métier. Il définit
 * une API générique, {@link factory.persistence.PersistenceStrategy}, permettant de :
 * </p>
 *
 * <ul>
 *     <li>Enregistrer un objet dans un fichier (persistant) ;</li>
 *     <li>Le recharger à partir de ce fichier ;</li>
 *     <li>Générer dynamiquement un nom de fichier à partir d’un identifiant logique.</li>
 * </ul>
 *
 * <p>
 * Une implémentation concrète est proposée avec {@link factory.persistence.XmlPersistenceStrategy},
 * qui repose sur le format XML, le DOM, et une DTD pour structurer les données. Elle utilise
 * des transformateurs ({@code XmlTransformer}) pour convertir les objets métiers en représentation
 * XML, renforçant ainsi l’extensibilité du système.
 * </p>
 *
 * <p>
 * Cette couche permet de conserver les profils de synchronisation et les registres
 * dans un format indépendant des classes métier, facilitant ainsi la maintenance,
 * la validation et l’évolution future vers d’autres formats (JSON, YAML, binaire...).
 * </p>
 *
 * @see factory.persistence.PersistenceStrategy
 * @see factory.persistence.XmlPersistenceStrategy
 * @since JDK 17
 */
package factory.persistence;