package factory.transformer;

import org.w3c.dom.*;

import model.Profile;

/**
 * Transformateur XML dédié à la conversion d’un objet {@link model.Profile} en document XML DOM, et inversement.
 * 
 * <p>
 * Les balises générées sont : 
 * <ol>
 *  <li>{@code <profile>},</li>
 *  <li>{@code <name>},</li>
 *  <li>{@code <pathA>},</li>
 *  <li>{@code <pathB>}.<li>
 * </ol>
 *
 * @see model.Profile
 * @see XmlTransformer
 * @since JDK 17
 */
public class ProfileXmlTransformer implements XmlTransformer<Profile> {

    @Override
    public Element toXml(Document doc, Profile profile) {
        // Création de l'élément racine <profile>
        Element root = doc.createElement("profile");

        // Ajout des éléments enfants : <name>, <pathA>, <pathB>
        root.appendChild(createTextElement(doc, "name", profile.getName()));
        root.appendChild(createTextElement(doc, "pathA", profile.getPathA()));
        root.appendChild(createTextElement(doc, "pathB", profile.getPathB()));

        return root;
    }

    @Override
    public Profile fromXml(Element root) {
        String name = getTextContent(root, "name");
        String pathA = getTextContent(root, "pathA");
        String pathB = getTextContent(root, "pathB");

        return new Profile(name, pathA, pathB);
    }

    @Override
    public String getRootTagName() {
        return "profile";
    }

    /**
     * Méthode utilitaire : crée un élément DOM avec une valeur textuelle.
     *
     * @param doc   document DOM
     * @param tag   nom de la balise
     * @param value texte à insérer dans la balise
     * @return élément DOM prêt à être inséré
     */
    private Element createTextElement(Document doc, String tag, String value) {
        Element element = doc.createElement(tag);
        element.setTextContent(value);
        return element;
    }

    /**
     * Méthode utilitaire : lit le contenu textuel d’un élément par son nom.
     *
     * @param parent élément contenant les balises recherchées
     * @param tag    nom de la balise
     * @return texte contenu dans la balise
     */
    private String getTextContent(Element parent, String tag) {
        return parent.getElementsByTagName(tag).item(0).getTextContent();
    }
}