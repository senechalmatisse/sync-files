package model;

import java.nio.file.Path;
import java.util.*;

import visitor.FileVisitor;

/**
 * Représente un dossier dans une arborescence de fichiers,
 * dans le cadre du patron de conception <strong>Composite</strong>.
 *
 * <p>
 * Un {@code DirectoryComposite} peut contenir une liste d’autres
 * composants {@link FileComponent}, qu’ils soient des fichiers
 * ({@link FileLeaf}) ou d'autres répertoires ({@code DirectoryComposite}).
 * </p>
 *
 * <p>
 * Il permet d’appliquer une opération à toute la hiérarchie via
 * le patron <strong>Visitor</strong>, en appelant récursivement
 * {@link FileComponent#accept(FileVisitor)} sur ses enfants.
 *
 * @see FileComponent
 * @see FileLeaf
 * @see FileVisitor
 * @since JDK 17
 */
public class DirectoryComposite implements FileComponent {
    /** Chemin absolu du répertoire représenté. */
    private final Path path;

    /** Liste des composants contenus dans ce répertoire. */
    private final List<FileComponent> children = new ArrayList<>();

    /**
     * Construit un composant représentant un répertoire.
     *
     * @param path chemin absolu du répertoire
     */
    public DirectoryComposite(Path path) {
        this.path = path;
    }

    /**
     * Ajoute un composant (fichier ou sous-répertoire) à ce dossier.
     *
     * @param component un élément enfant du répertoire
     */
    public void add(FileComponent component) {
        children.add(component);
    }

    /**
     * Retourne la liste des composants enfants de ce répertoire.
     *
     * @return liste des enfants (fichiers ou dossiers)
     */
    public List<FileComponent> getChildren() {
        return children;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public String getName() {
        return path.getFileName().toString();
    }

    @Override
    public void accept(FileVisitor visitor) {
        for (FileComponent child : children) {
            child.accept(visitor);
        }
    }
}