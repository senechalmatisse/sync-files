/**
 * Fournit les classes de gestion centrale pour la persistance et la coordination
 * des entités de l'application, telles que les profils de synchronisation et les registres.
 *
 * <p>
 * Le package {@code manager} applique le patron de conception <strong>Singleton</strong>
 * pour offrir un point d'accès unique et cohérent aux ressources critiques :
 * </p>
 * <ul>
 *     <li>{@link manager.ProfileManager} : gestion de la création, sauvegarde et lecture des profils</li>
 *     <li>{@link manager.RegistryManager} : gestion du registre d'historique de synchronisation des fichiers</li>
 * </ul>
 *
 * <p>
 * Ces gestionnaires sont configurables dynamiquement grâce au patron <strong>Strategy</strong>,
 * à travers l'utilisation de l'interface {@link factory.persistence.PersistenceStrategy}. Ainsi,
 * les formats de persistance (XML, JSON, etc.) peuvent être échangés sans modifier
 * la logique métier.
 * </p>
 *
 * <p>
 * Le répertoire {@code profiles/} contient les profils de synchronisation persistés, tandis que
 * le répertoire {@code registries/} contient les registres liés à chaque profil.
 * </p>
 *
 * <h2>Principaux rôles</h2>
 * <ul>
 *     <li>Encapsulation des opérations d’E/S sur disque</li>
 *     <li>Gestion de la cohérence d’accès via une instance unique</li>
 *     <li>Soutien aux autres composants (ex : {@code SyncApp}, {@code SyncHandler})</li>
 * </ul>
 *
 * @since JDK 17
 */
package manager;