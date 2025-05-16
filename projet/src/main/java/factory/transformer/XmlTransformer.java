package factory.transformer;

import org.w3c.dom.*;

/**
 * Interface générique de transformation entre un objet métier et sa représentation XML DOM.
 *
 * <p>
 * Cette interface définit un contrat de sérialisation et désérialisation XML
 * pour un type de données métier {@code T}. Elle permet de produire des
 * éléments DOM à partir d'objets Java, et de reconstruire ces derniers
 * à partir de leur équivalent XML.
 * </p>
 *
 * <p>
 * Elle joue un rôle de <strong>Data Mapper</strong> dédié à la persistance XML.
 *
 * @param <T> le type d’objet métier concerné
 *
 * @see ProfileXmlTransformer
 * @see RegistryXmlTransformer
 * @since JDK 17
 */
public interface XmlTransformer<T> {

    /**
     * Convertit un objet métier de type {@code T} en élément DOM XML.
     *
     * @param doc    document DOM utilisé pour la création des éléments
     * @param object l'objet à convertir en XML
     * @return élément DOM représentant l'objet {@code T}
     */
    Element toXml(Document doc, T object);

    /**
     * Reconstruit un objet métier de type {@code T} à partir d’un élément DOM XML.
     *
     * @param element l'élément XML source
     * @return l'objet {@code T} reconstruit à partir du XML
     */
    T fromXml(Element element);

    /**
     * Retourne le nom de la balise racine associée à ce type d’objet.
     *
     * @return nom de la balise racine (ex. "profile", "registry")
     */
    String getRootTagName();
}