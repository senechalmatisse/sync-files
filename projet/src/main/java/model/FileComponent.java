package model;

import java.nio.file.Path;

import visitor.*;

/**
 * Interface représentant un composant dans une arborescence de fichiers,
 * dans le cadre du patron de conception <strong>Composite</strong>.
 *
 * <p>
 * Un {@code FileComponent} peut être une feuille ({@link FileLeaf})
 * représentant un fichier, ou un composite ({@link DirectoryComposite})
 * représentant un dossier contenant d'autres composants.
 * </p>
 *
 * <p>
 * Cette abstraction permet de traiter de manière uniforme les fichiers et
 * répertoires dans les opérations de parcours, notamment avec le patron
 * <strong>Visitor</strong>.
 *
 * @see FileLeaf
 * @see DirectoryComposite
 * @see FileVisitor
 * @since JDK 17
 */
public interface FileComponent {

    /**
     * Retourne le chemin absolu du fichier ou du dossier.
     *
     * @return le chemin du composant
     */
    Path getPath();

    /**
     * Retourne le nom du fichier ou du dossier.
     *
     * @return le nom de l’entrée
     */
    String getName();

    /**
     * Accepte un visiteur pour appliquer une opération à ce composant.
     *
     * @param visitor le visiteur à appliquer
     */
    void accept(FileVisitor visitor);
}