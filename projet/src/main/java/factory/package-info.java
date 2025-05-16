/**
 * Fournit des fabriques pour la création de stratégies de persistance adaptées
 * à différents types d’objets métiers, selon le patron de conception
 * <strong>Factory Method</strong>.
 *
 * <p>
 * Le package {@code factory} a pour but de découpler les composants métiers
 * de la logique de sérialisation. Il permet de fournir dynamiquement des instances
 * de {@link factory.persistence.PersistenceStrategy}, configurées pour manipuler un format
 * spécifique (XML, JSON, etc.) et un type donné (ex : {@link model.Profile}, {@link model.Registry}).
 * </p>
 *
 * <p>
 * Ce mécanisme permet d’adopter facilement une nouvelle technologie de persistance
 * sans modifier les gestionnaires métiers (comme {@code ProfileManager} ou {@code RegistryManager}),
 * en assurant un haut niveau d’extensibilité et de réutilisabilité.
 * </p>
 *
 * <h2>Composants clés</h2>
 * <ul>
 *     <li>{@link factory.PersistenceStrategyFactory} : interface de fabrique générique.</li>
 *     <li>{@link factory.XmlProfileStrategyFactory} : fabrique XML pour les profils.</li>
 *     <li>{@link factory.XmlRegistryStrategyFactory} : fabrique XML pour les registres.</li>
 * </ul>
 *
 * <p>
 * Ce package est étroitement lié à {@code persistence} et {@code transformer},
 * qui assurent la sérialisation et la conversion DOM XML.
 *
 * @since JDK 17
 */
package factory;