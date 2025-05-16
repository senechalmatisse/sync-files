package visitor;

import model.*;

/**
 * Interface du patron de conception <strong>Visitor</strong> appliquée à une arborescence de fichiers.
 *
 * <p>
 * Définit les opérations disponibles pour parcourir des fichiers.
 * </p>
 *
 * <p>
 * Ce contrat permet de séparer la structure de l’arborescence (modèle Composite)
 * de la logique appliquée à chaque élément (ex : synchronisation, affichage, comptage...).
 *
 * @see model.FileComponent
 * @since JDK 17
 */
public interface FileVisitor {

    /**
     * Visite un fichier.
     *
     * @param file le fichier à visiter
     */
    void visit(FileComponent file);
}