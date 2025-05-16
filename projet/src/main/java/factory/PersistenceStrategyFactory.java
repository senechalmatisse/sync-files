package factory;

import factory.persistence.PersistenceStrategy;

/**
 * Interface du patron <strong>Factory Method</strong> pour créer une stratégie
 * de persistance spécifique à un type d’objet.
 *
 * <p>
 * Cette interface permet de produire dynamiquement des instances de {@link PersistenceStrategy}
 * en fonction du type d’objet manipulé. Cela permet de découpler les composants métiers
 * de la technologie de sérialisation (XML, JSON, etc.).
 * </p>
 *
 * @param <T> le type d’objet manipulé par la stratégie de persistance
 *
 * @see PersistenceStrategy
 * @since JDK 17
 */
public interface PersistenceStrategyFactory<T> {

    /**
     * Crée une instance de stratégie de persistance adaptée au type {@code T}.
     *
     * @return une stratégie de persistance
     */
    PersistenceStrategy<T> createStrategy();
}