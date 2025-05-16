/**
 * Fournit des classes utilitaires pour la conversion entre objets métier et représentation XML DOM.
 *
 * <p>
 * Ce package regroupe des composants responsables de la transformation de structures de données internes
 * (comme {@link model.Profile} ou {@link model.Registry}) en documents XML DOM, et inversement.
 * </p>
 *
 * <p>
 * Contrairement à ce que leur nom pourrait suggérer, ces classes ne relèvent pas du patron <em>Adapter</em>
 * tel que défini dans la littérature sur les patrons de conception (GoF), mais correspondent plutôt à une
 * logique de type <strong>Data Mapper</strong> ou <strong>Serializer/Deserializer</strong>, dédiée
 * à la sérialisation.
 * </p>
 *
 * <p>
 * Les implémentations fournies sont :
 * <ul>
 *     <li>{@link factory.transformer.ProfileXmlTransformer} — pour les objets {@link model.Profile},</li>
 *     <li>{@link factory.transformer.RegistryXmlTransformer} — pour les objets {@link model.Registry}.</li>
 * </ul>
 * Ces classes sont exploitées par le package {@link factory.persistence} pour permettre une persistance
 * lisible (au format XML).
 * </p>
 *
 * @since JDK 17
 */
package factory.transformer;