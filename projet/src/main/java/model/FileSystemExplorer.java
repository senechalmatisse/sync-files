package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * Classe utilitaire permettant d'explorer récursivement un répertoire du système de fichiers
 * et de construire une structure Composite représentant son contenu.
 *
 * <p>
 * Chaque dossier est représenté par un objet {@link DirectoryComposite} et chaque fichier par
 * un objet {@link FileLeaf}. Cette classe est utilisée notamment dans le cadre du programme
 * de synchronisation pour représenter l’état des dossiers A et B avant traitement.
 *
 * @see DirectoryComposite
 * @see FileLeaf
 * @see FileComponent
 * @since JDK 17
 */
public class FileSystemExplorer {

    /**
     * Point d'entrée pour explorer un répertoire donné.
     *
     * @param root le chemin du répertoire à explorer
     * @return un {@link DirectoryComposite} représentant le contenu
     * @throws IOException si erreur de lecture
     */
    public static FileComponent explore(Path root) throws IOException {
        validateDirectory(root);
        return buildDirectory(root);
    }

    /**
     * Valide que le chemin fourni est un dossier existant.
     *
     * @param root chemin à valider
     * @throws IllegalArgumentException si ce n’est pas un dossier
     * @throws IOException si le contenu ne peut pas être listé
     */
    private static void validateDirectory(Path root) throws IOException {
        File rootFile = root.toFile();
        if (!rootFile.isDirectory()) {
            throw new IllegalArgumentException("Le chemin fourni n'est pas un dossier : " + root);
        }
        if (rootFile.listFiles() == null) {
            throw new IOException("Impossible de lister le contenu du dossier : " + root);
        }
    }

    /**
     * Construit récursivement la structure Composite d’un répertoire.
     *
     * @param root dossier à explorer
     * @return {@link FileComponent} représentant ce dossier
     * @throws IOException si erreur de lecture
     */
    private static FileComponent buildDirectory(Path root) throws IOException {
        DirectoryComposite dir = new DirectoryComposite(root);
        File[] children = root.toFile().listFiles();

        for (File child : children) {
            Path childPath = child.toPath();

            if (Files.isSymbolicLink(childPath)) {
                continue; // on ignore les liens symboliques
            }

            if (child.isDirectory()) {
                dir.add(buildDirectory(childPath));
            } else if (child.isFile()) {
                dir.add(new FileLeaf(childPath));
            }
        }

        return dir;
    }
}