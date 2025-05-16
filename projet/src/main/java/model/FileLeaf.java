package model;

import java.nio.file.Path;

import visitor.FileVisitor;

/**
 * Classe représentant une feuille dans l'arborescence de fichiers,
 * correspondant à un fichier concret du système de fichiers.
 *
 * <p>
 * Cette classe implémente l’interface {@link FileComponent} dans le cadre
 * du patron <strong>Composite</strong>. Contrairement à un répertoire
 * ({@link DirectoryComposite}), une feuille ne contient aucun sous-élément.
 * </p>
 *
 * <p>
 * Elle prend également en charge le patron <strong>Visitor</strong>, en
 * redirigeant l'appel vers la méthode {@link FileVisitor#visit(FileLeaf)}.
 *
 * @see FileComponent
 * @see DirectoryComposite
 * @see FileVisitor
 * @since JDK 17
 */
public class FileLeaf implements FileComponent {
    /** Chemin absolu vers le fichier représenté. */
    private final Path path;

    /**
     * Construit une feuille représentant un fichier.
     *
     * @param path chemin absolu vers le fichier
     */
    public FileLeaf(Path path) {
        this.path = path;
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
        visitor.visit(this);
    }
}