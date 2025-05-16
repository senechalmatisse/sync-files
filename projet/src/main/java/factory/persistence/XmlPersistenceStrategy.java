package factory.persistence;

import org.w3c.dom.*;

import factory.transformer.XmlTransformer;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.nio.file.*;

/**
 * Stratégie de persistance basée sur XML.
 *
 * <p>
 * Implémente l’interface {@link PersistenceStrategy} pour sauvegarder
 * et recharger des objets sous forme de fichiers XML.
 * </p>
 *
 * <p>
 * Lors de la sauvegarde, une déclaration <strong>DOCTYPE</strong> est insérée
 * pour référencer automatiquement une DTD située dans les ressources du projet.
 * Cette DTD est copiée à côté du fichier XML pour permettre la validation ultérieure.
 *
 * @param <T> le type d’objet à sérialiser
 *
 * @see XmlTransformer
 * @since JDK 17
 */
public class XmlPersistenceStrategy<T> implements PersistenceStrategy<T> {
    /** Adaptateur responsable de la conversion entre l'objet métier et sa représentation XML. */
    private final XmlTransformer<T> adapter;

    /** Extension de fichier utilisée pour les fichiers générés (ex. {@code xml}). */
    private final String fileExtension;

    /**
     * Construit une stratégie de persistance XML avec un adaptateur spécifique.
     *
     * @param adapter        convertisseur entre l'objet métier et XML
     * @param fileExtension  extension de fichier (sans le point)
     */
    public XmlPersistenceStrategy(XmlTransformer<T> adapter, String fileExtension) {
        this.adapter = adapter;
        this.fileExtension = fileExtension;
    }

    @Override
    public void save(String path, T value) throws IOException {
        Path outputPath = Paths.get(path);
        String rootName;

        try {
            // Création du document DOM vide
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            // Génère l'élément racine à partir de l'objet métier
            Element root = adapter.toXml(doc, value);
            rootName = adapter.getRootTagName();
            doc.appendChild(root); // Ajoute l'élément racine au document

            // Nom de fichier DTD attendu : <racine>.dtd (ex: profile.dtd)
            String dtdFile = rootName + ".dtd";

            // Copie la DTD dans le même dossier que le XML si elle existe
            copyDtdIfNeeded(dtdFile, outputPath.getParent());

            // Prépare le transformateur pour écrire le fichier XML
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdFile);
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // Écrit le contenu du document XML dans le fichier cible
            transformer.transform(new DOMSource(doc), new StreamResult(outputPath.toFile()));
        } catch (Exception e) {
            throw new IOException("Erreur lors de la sauvegarde XML : " + e.getMessage(), e);
        }
    }

    @Override
    public T load(String path, Class<T> type) throws IOException {
        try {
            // Charge le fichier XML en mémoire
            File file = new File(path);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file);

            // Reconstruit l’objet métier à partir de l’élément racine
            Element root = doc.getDocumentElement();
            return adapter.fromXml(root);
        } catch (Exception e) {
            throw new IOException("Erreur lors du chargement XML", e);
        }
    }

    @Override
    public String getFileName(String name) {
        return name + "." + fileExtension;
    }

    /**
     * Copie le fichier DTD depuis les ressources vers le dossier cible si nécessaire.
     *
     * @param dtdFile     le nom du fichier DTD (ex. "profile.dtd")
     * @param destination le dossier cible où le placer
     * @throws IOException si une erreur de copie survient
     */
    private void copyDtdIfNeeded(String dtdFile, Path destination) throws IOException {
        try (InputStream dtdInput = getClass().getClassLoader().getResourceAsStream(dtdFile)) {
            if (dtdInput == null) {
                System.err.println("DTD introuvable dans les ressources : " + dtdFile);
                return;
            }

            // Écriture du fichier DTD à côté du fichier XML généré
            Path target = destination.resolve(dtdFile);
            Files.copy(dtdInput, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}