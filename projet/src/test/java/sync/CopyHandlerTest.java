package sync;

import model.Registry;

import manager.RegistryManager;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CopyHandlerTest {

    private Path dirA;
    private Path dirB;
    private Registry registry;
    private CopyHandler handler;

    @BeforeEach
    void setup() throws IOException {
        dirA = Files.createTempDirectory("copy-A");
        dirB = Files.createTempDirectory("copy-B");
        registry = new Registry("testProfile");
        handler = new CopyHandler();
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(dirA)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
        Files.walk(dirB)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    void givenFileInAIsNewer_whenHandle_thenCopiedToBAndRegistryUpdated() throws IOException, InterruptedException {
        // GIVEN
        Path fileB = Files.writeString(dirB.resolve("file.txt"), "Old from B");
        Thread.sleep(50);
        Path fileA = Files.writeString(dirA.resolve("file.txt"), "New from A");

        SyncContext context = new SyncContext(fileA, fileB, "file.txt", registry);

        try (MockedStatic<RegistryManager> mocked = mockStatic(RegistryManager.class)) {
            RegistryManager mockManager = mock(RegistryManager.class);
            mocked.when(RegistryManager::getInstance).thenReturn(mockManager);

            // WHEN
            handler.handle(context);

            // THEN
            assertEquals("New from A", Files.readString(fileB));
            assertTrue(registry.contains("file.txt"));
            verify(mockManager).saveRegistry(registry);
        }
    }


    @Test
    void givenFileInBIsNewer_whenHandle_thenCopiedToAAndRegistryUpdated() throws IOException, Throwable {
        // GIVEN
        Path fileA = Files.writeString(dirA.resolve("file.txt"), "Old from A");
        Thread.sleep(50);
        Path fileB = Files.writeString(dirB.resolve("file.txt"), "New from B");

        SyncContext context = new SyncContext(fileA, fileB, "file.txt", registry);

        try (MockedStatic<RegistryManager> mocked = mockStatic(RegistryManager.class)) {
            RegistryManager mockManager = mock(RegistryManager.class);
            mocked.when(RegistryManager::getInstance).thenReturn(mockManager);

            // WHEN
            handler.handle(context);

            // THEN
            assertEquals("New from B", Files.readString(fileA));
            assertTrue(registry.contains("file.txt"));
            verify(mockManager).saveRegistry(registry);
        }
    }

    @Test
    void givenSimilarTimestamps_whenHandle_thenNoCopyPerformed() throws IOException {
        // GIVEN
        Path fileA = Files.writeString(dirA.resolve("file.txt"), "Same time");
        Path fileB = Files.writeString(dirB.resolve("file.txt"), "Same time");

        SyncContext context = new SyncContext(fileA, fileB, "file.txt", registry);

        try (MockedStatic<RegistryManager> mocked = mockStatic(RegistryManager.class)) {
            RegistryManager mockManager = mock(RegistryManager.class);
            mocked.when(RegistryManager::getInstance).thenReturn(mockManager);

            // WHEN
            handler.handle(context);

            // THEN
            verify(mockManager, never()).saveRegistry(any());
        }
    }

    @Test
    void givenMissingFileInAOrB_whenHandle_thenHandlerSkipsAndDoesNotFail() throws IOException {
        // GIVEN : file exists only in A
        Path fileA = Files.writeString(dirA.resolve("file.txt"), "Only A");
        Path fileB = dirB.resolve("file.txt"); // non créé

        SyncContext context = new SyncContext(fileA, fileB, "file.txt", registry);

        // WHEN / THEN
        assertDoesNotThrow(() -> handler.handle(context));
        assertFalse(Files.exists(fileB)); // Pas de copie faite
    }
}