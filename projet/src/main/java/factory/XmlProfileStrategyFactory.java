package factory;

import factory.persistence.*;
import factory.transformer.ProfileXmlTransformer;
import model.Profile;

/**
 * Fabrique concrète implémentant {@link PersistenceStrategyFactory}
 * pour produire une stratégie de persistance XML destinée aux objets {@link Profile}.
 *
 * <p>
 * Cette classe concrétise le patron <strong>Factory Method</strong> en encapsulant
 * la logique de création d’un {@link XmlPersistenceStrategy} adapté aux profils.
 * </p>
 *
 * @see Profile
 * @see XmlPersistenceStrategy
 * @see factory.transformer.ProfileXmlTransformer
 * @since JDK 17
 */
public class XmlProfileStrategyFactory implements PersistenceStrategyFactory<Profile> {

    @Override
    public PersistenceStrategy<Profile> createStrategy() {
        return new XmlPersistenceStrategy<>(new ProfileXmlTransformer(), "xml");
    }
}