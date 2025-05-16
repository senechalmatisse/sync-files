package factory;

import factory.persistence.*;
import factory.transformer.RegistryXmlTransformer;
import model.Registry;

/**
 * Fabrique concrète implémentant {@link PersistenceStrategyFactory}
 * pour produire une stratégie de persistance XML destinée aux objets {@link Registry}.
 *
 * <p>
 * Cette classe concrétise le patron <strong>Factory Method</strong> en encapsulant
 * la logique de création d’un {@link XmlPersistenceStrategy} adapté aux registres.
 * </p>
 *
 * @see Registry
 * @see XmlPersistenceStrategy
 * @see factory.transformer.RegistryXmlTransformer
 * @since JDK 17
 */
public class XmlRegistryStrategyFactory implements PersistenceStrategyFactory<Registry> {

    @Override
    public PersistenceStrategy<Registry> createStrategy() {
        return new XmlPersistenceStrategy<>(new RegistryXmlTransformer(), "xml");
    }
}