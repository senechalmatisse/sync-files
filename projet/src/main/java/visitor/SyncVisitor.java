package visitor;

import filesystem.FileHandler;
import model.*;
import sync.*;

import java.nio.file.Path;

/**
 * Visiteur chargé de déclencher la synchronisation sur une arborescence de fichiers.
 *
 * <p>
 * Implémente le patron de conception <strong>Visitor</strong> pour appliquer
 * une logique de synchronisation sur des composants d’arborescence {@link FileComponent}.
 * </p>
 *
 * <p>
 * Pour chaque fichier, une instance de {@link SyncContext} est créée et transmise
 * à la chaîne {@link SyncHandler}, permettant de déclencher les traitements
 * (copie, suppression, conflit, enregistrement...).
 * </p>
 *
 * @see sync.SyncHandler
 * @see sync.SyncContext
 * @see model.FileComponent
 * @since JDK 17
 */
public class SyncVisitor implements FileVisitor {
    /** Chemin absolu de la racine du répertoire A. */
    private final Path baseA;

    /** Chemin absolu de la racine du répertoire B. */
    private final Path baseB;

    /** Chaîne de traitement (copie, suppression, conflit...) appliquée à chaque fichier. */
    private final SyncHandler handler;

    /** Interface d’abstraction pour manipuler les fichiers (local ou distant (à l'avenir)). */
    private final FileHandler fileHandler;

    /** Registre des dernières synchronisations associées au profil en cours. */
    private final Registry registry;

    /**
     * Construit un visiteur de synchronisation avec les dépendances nécessaires.
     *
     * @param baseA       chemin racine du répertoire A
     * @param baseB       chemin racine du répertoire B
     * @param handler     la chaîne de responsabilité à appliquer à chaque fichier
     * @param fileHandler l’accès au système de fichiers (abstrait)
     * @param registry    le registre associé au profil de synchronisation
     */
    public SyncVisitor(Path baseA, Path baseB, SyncHandler handler, FileHandler fileHandler, Registry registry) {
        this.baseA = baseA;
        this.baseB = baseB;
        this.handler = handler;
        this.fileHandler = fileHandler;
        this.registry = registry;
    }    

    @Override
    public void visit(FileComponent file) {
        Path absolutePathA = file.getPath();
        Path relative = baseA.relativize(absolutePathA);
        Path absolutePathB = baseB.resolve(relative);
        String relativePath = relative.toString().replace("\\", "/"); // pour compatibilité

        SyncContext context = new SyncContext(absolutePathA, absolutePathB, relativePath, registry);
        handler.handle(context); // lance la chaîne
    }
}