package factory.transformer;

import model.Registry;

import org.w3c.dom.*;

import java.util.Map;

/**
 * Transformateur XML concret pour la classe {@link model.Registry}.
 *
 * <p>
 * Chaque entrée est représentée par un élément {@code <entry>} contenant
 * un {@code <path>} et un {@code <timestamp>}.
 *
 * @see model.Registry
 * @see XmlTransformer
 * @since JDK 17
 */
public class RegistryXmlTransformer implements XmlTransformer<Registry> {

    @Override
    public Element toXml(Document doc, Registry registry) {
        // Création de l'élément racine <registry>
        Element root = doc.createElement("registry");
        // Ajout d'un attribut "profile" pour identifier le nom du profil lié au registre
        root.setAttribute("profile", registry.getProfileName());

        // Parcours de toutes les entrées (fichier -> timestamp) dans le registre
        for (Map.Entry<String, Long> entry : registry.getEntries().entrySet()) {
            // Création de l'élément <entry> pour une paire (path, timestamp)
            Element entryElem = doc.createElement("entry");

            entryElem.appendChild(createTextElement(doc, "path", entry.getKey()));
            entryElem.appendChild(createTextElement(doc, "timestamp", String.valueOf(entry.getValue())));

            root.appendChild(entryElem);
        }

        return root;
    }

    @Override
    public Registry fromXml(Element root) {
        // Lecture de l'attribut "profile"
        Registry registry = new Registry(root.getAttribute("profile"));
        // Récupération de tous les éléments <entry> du registre
        NodeList entryNodes = root.getElementsByTagName("entry");

        for (int i = 0; i < entryNodes.getLength(); i++) {
            Element entryElem = (Element) entryNodes.item(i);

            // Lecture de <path> (chemin relatif)
            String path = entryElem.getElementsByTagName("path").item(0).getTextContent();
            // Lecture de <timestamp> et conversion en long
            long timestamp = Long.parseLong(
                entryElem.getElementsByTagName("timestamp").item(0).getTextContent()
            );

            registry.put(path, timestamp);
        }

        return registry;
    }

    @Override
    public String getRootTagName() {
        return "registry";
    }

    /**
     * Crée un élément XML avec un nom donné et une valeur textuelle.
     *
     * @param doc   document DOM
     * @param tag   nom de la balise
     * @param value contenu textuel
     * @return élément DOM prêt à être inséré
     */
    private Element createTextElement(Document doc, String tag, String value) {
        Element elem = doc.createElement(tag);
        elem.setTextContent(value);
        return elem;
    }
}