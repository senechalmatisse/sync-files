package filesystem;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class LocalFileHandlerTest {
    private LocalFileHandler fileHandler;
    private Path tempFile;
    private Path copyFile;

    @BeforeEach
    void setUp() throws IOException {
        fileHandler = new LocalFileHandler();
        tempFile = Files.createTempFile("filehandler-test", ".txt");
        Files.writeString(tempFile, "Hello!");
        copyFile = tempFile.resolveSibling("copy.txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
        Files.deleteIfExists(copyFile);
    }

    @Test
    void shouldCopyFile() throws IOException {
        fileHandler.copy(tempFile, copyFile);
        assertTrue(Files.exists(copyFile));
        assertEquals(Files.readString(tempFile), Files.readString(copyFile));
    }

    @Test
    void shouldDeleteFile() throws IOException {
        fileHandler.delete(tempFile);
        assertFalse(Files.exists(tempFile));
    }

    @Test
    void shouldReturnLastModifiedTime() throws IOException {
        long modified = fileHandler.getLastModified(tempFile);
        assertTrue(modified > 0);
    }

    @Test
    void shouldReturnTrueIfFileExists() {
        assertTrue(fileHandler.exists(tempFile));
        assertFalse(fileHandler.exists(Path.of("not_exists.txt")));
    }
}