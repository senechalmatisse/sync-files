package sync;

import model.Registry;
import manager.*;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.*;
import java.nio.file.*;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterHandlerTest {

    private Path dirA;
    private Path dirB;
    private Registry registry;
    private RegisterHandler handler;

    @BeforeEach
    public void setup() throws IOException {
        dirA = Files.createTempDirectory("A");
        dirB = Files.createTempDirectory("B");
        registry = new Registry("testProfile");
        handler = new RegisterHandler();
    }

    @AfterEach
    public void tearDown() throws IOException {
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
    public void givenFileOnlyInA_whenNotRegistered_thenCopiedToBAndRegistered() throws IOException {
        // GIVEN
        Path fileA = Files.writeString(dirA.resolve("file.txt"), "From A");
        Path fileB = dirB.resolve("file.txt");
        SyncContext context = new SyncContext(fileA, fileB, "file.txt", registry);

        try (MockedStatic<RegistryManager> mocked = mockStatic(RegistryManager.class)) {
            RegistryManager mockManager = mock(RegistryManager.class);
            mocked.when(RegistryManager::getInstance).thenReturn(mockManager);

            // WHEN
            handler.handle(context);

            // THEN
            assertTrue(Files.exists(fileB));
            assertTrue(registry.contains("file.txt"));
            verify(mockManager).saveRegistry(registry);
        }
    }

    @Test
    public void givenFileOnlyInB_whenNotRegistered_thenCopiedToAAndRegistered() throws IOException {
        // GIVEN
        Path fileB = Files.writeString(dirB.resolve("file.txt"), "From B");
        Path fileA = dirA.resolve("file.txt");
        SyncContext context = new SyncContext(fileA, fileB, "file.txt", registry);

        try (MockedStatic<RegistryManager> mocked = mockStatic(RegistryManager.class)) {
            RegistryManager mockManager = mock(RegistryManager.class);
            mocked.when(RegistryManager::getInstance).thenReturn(mockManager);

            // WHEN
            handler.handle(context);

            // THEN
            assertTrue(Files.exists(fileA));
            assertTrue(registry.contains("file.txt"));
            verify(mockManager).saveRegistry(registry);
        }
    }

    @Test
    public void givenFileAlreadyRegistered_whenHandle_thenDoNothing() throws IOException {
        // GIVEN
        Path fileA = Files.writeString(dirA.resolve("file.txt"), "From A");
        Path fileB = dirB.resolve("file.txt");
        registry.put("file.txt", System.currentTimeMillis());
        SyncContext context = new SyncContext(fileA, fileB, "file.txt", registry);

        try (MockedStatic<RegistryManager> mocked = mockStatic(RegistryManager.class)) {
            RegistryManager mockManager = mock(RegistryManager.class);
            mocked.when(RegistryManager::getInstance).thenReturn(mockManager);

            // WHEN
            handler.handle(context);

            // THEN
            assertFalse(Files.exists(fileB)); // pas copié
            verify(mockManager, never()).saveRegistry(any());
        }
    }

    @Test
    public void givenIOExceptionDuringSaveRegistry_whenHandle_thenErrorHandledGracefully() throws IOException {
        // GIVEN
        Path fileA = Files.writeString(dirA.resolve("file.txt"), "Erreur de test");
        Path fileB = dirB.resolve("file.txt");
        SyncContext context = new SyncContext(fileA, fileB, "file.txt", registry);

        // Rediriger System.err pour capter le message
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;
        System.setErr(new PrintStream(errContent));

        try (MockedStatic<RegistryManager> mocked = mockStatic(RegistryManager.class)) {
            RegistryManager spyManager = mock(RegistryManager.class);
            mocked.when(RegistryManager::getInstance).thenReturn(spyManager);

            doThrow(new IOException("Erreur volontaire")).when(spyManager).saveRegistry(any());

            // WHEN / THEN
            assertDoesNotThrow(() -> handler.handle(context),
                "Le handler doit capturer proprement l'IOException");

            String errOutput = errContent.toString();
            assertTrue(errOutput.contains("Erreur lors de l'enregistrement"), "Le message d'erreur doit être affiché.");
        } finally {
            System.setErr(originalErr);
        }
    }
}