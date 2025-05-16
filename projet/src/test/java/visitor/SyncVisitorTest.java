package visitor;

import filesystem.FileHandler;
import model.*;
import sync.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.mockito.Mockito.*;

public class SyncVisitorTest {

    private SyncHandler handler;
    private FileHandler fileHandler;
    private Registry registry;
    private Path baseA;
    private Path baseB;
    private SyncVisitor visitor;

    @BeforeEach
    public void setup() {
        handler = mock(SyncHandler.class);
        fileHandler = mock(FileHandler.class);
        registry = new Registry("testProfile");
        baseA = Path.of("/base/A");
        baseB = Path.of("/base/B");

        visitor = new SyncVisitor(baseA, baseB, handler, fileHandler, registry);
    }

    @Test
    public void givenFileLeaf_whenVisited_thenHandlerIsInvokedWithCorrectContext() {
        // GIVEN
        Path filePathA = baseA.resolve("docs/test.txt");
        FileLeaf fileLeaf = new FileLeaf(filePathA);

        // WHEN
        visitor.visit(fileLeaf);

        // THEN
        verify(handler, times(1)).handle(argThat(context ->
                context.pathA.equals(filePathA) &&
                context.pathB.equals(baseB.resolve("docs/test.txt")) &&
                context.relativePath.equals("docs/test.txt") &&
                context.registry == registry
        ));
    }

    @Test
    public void givenDirectoryComposite_whenVisited_thenPrintsDirectoryName() {
        // GIVEN
        Path directoryPath = baseA.resolve("images");
        DirectoryComposite directory = new DirectoryComposite(directoryPath);

        // WHEN / THEN
        // Pas de traitement métier à tester, juste pas d'exception
        visitor.visit(directory);
    }
}