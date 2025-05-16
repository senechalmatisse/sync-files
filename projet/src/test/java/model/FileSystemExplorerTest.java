package model;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class FileSystemExplorerTest {

    private Path tempDir;

    @BeforeEach
    public void setup() throws IOException {
        tempDir = Files.createTempDirectory("explorerTest");
    }

    @AfterEach
    public void cleanup() throws IOException {
        if (tempDir != null && Files.exists(tempDir)) {
            Files.walk(tempDir)
                    .sorted(Comparator.reverseOrder())
                    .forEach(p -> p.toFile().delete());
        }
    }

    @Test
    public void givenDirectoryWithFilesAndSubdirs_whenExplore_thenCompositeIsBuiltCorrectly() throws IOException {
        // GIVEN
        Path subDir = Files.createDirectory(tempDir.resolve("sub"));
        @SuppressWarnings("unused")
        Path file1 = Files.createFile(tempDir.resolve("a.txt"));
        @SuppressWarnings("unused")
        Path file2 = Files.createFile(subDir.resolve("b.txt"));

        // WHEN
        FileComponent root = FileSystemExplorer.explore(tempDir);

        // THEN
        assertInstanceOf(DirectoryComposite.class, root);
        DirectoryComposite rootDir = (DirectoryComposite) root;

        assertEquals(2, rootDir.getChildren().size()); // a.txt + sub/

        boolean hasFile = rootDir.getChildren().stream().anyMatch(f -> f.getPath().endsWith("a.txt"));
        boolean hasSubdir = rootDir.getChildren().stream().anyMatch(f -> f instanceof DirectoryComposite);

        assertTrue(hasFile, "Le fichier a.txt devrait être trouvé");
        assertTrue(hasSubdir, "Le dossier sub/ devrait être trouvé");

        DirectoryComposite sub = (DirectoryComposite) rootDir.getChildren().stream()
                .filter(f -> f instanceof DirectoryComposite)
                .findFirst()
                .orElseThrow();

        assertEquals(1, sub.getChildren().size());
        assertTrue(sub.getChildren().get(0).getPath().endsWith("b.txt"));
    }

    @Test
    public void givenDirectoryWithOnlyFiles_whenExplore_thenOnlyLeavesAreReturned() throws IOException {
        // GIVEN
        Files.createFile(tempDir.resolve("x.txt"));
        Files.createFile(tempDir.resolve("y.txt"));

        // WHEN
        FileComponent root = FileSystemExplorer.explore(tempDir);

        // THEN
        assertInstanceOf(DirectoryComposite.class, root);
        DirectoryComposite dir = (DirectoryComposite) root;
        assertEquals(2, dir.getChildren().size());
        assertTrue(dir.getChildren().stream().allMatch(c -> c instanceof FileLeaf));
    }

    @Test
    public void givenEmptyDirectory_whenExplore_thenEmptyCompositeReturned() throws IOException {
        // WHEN
        FileComponent root = FileSystemExplorer.explore(tempDir);

        // THEN
        assertInstanceOf(DirectoryComposite.class, root);
        DirectoryComposite dir = (DirectoryComposite) root;
        assertTrue(dir.getChildren().isEmpty());
    }

    @Test
    public void givenNonDirectoryPath_whenExplore_thenThrowsIllegalArgumentException() throws IOException {
        // GIVEN
        Path file = Files.createTempFile("fake", ".txt");

        // WHEN / THEN
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            FileSystemExplorer.explore(file);
        });

        assertTrue(exception.getMessage().contains("n'est pas un dossier"));

        Files.delete(file); // nettoyage
    }

    @Test
    public void givenDirectoryWithSymlink_whenExplore_thenSymlinkIsIgnored() throws IOException {
        // GIVEN
        Path target = Files.createFile(tempDir.resolve("target.txt"));
        @SuppressWarnings("unused")
        Path symlink = Files.createSymbolicLink(tempDir.resolve("link.txt"), target);

        // WHEN
        FileComponent root = FileSystemExplorer.explore(tempDir);

        // THEN
        DirectoryComposite dir = (DirectoryComposite) root;

        // Seul le fichier réel doit être pris
        assertEquals(1, dir.getChildren().size());
        assertTrue(dir.getChildren().get(0).getPath().endsWith("target.txt"));
    }
}