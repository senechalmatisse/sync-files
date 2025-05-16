package app;

import org.junit.jupiter.api.*;
import org.mockito.*;

import visitor.FileVisitor;
import filesystem.*;
import manager.*;
import model.*;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SyncAppTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setupStreams() {
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void givenNoArgs_whenMain_thenUsageMessagePrinted() {
        // WHEN
        SyncApp.main(new String[]{});

        // THEN
        assertTrue(out.toString().contains("Usage"), "Doit afficher un message d’usage.");
    }

    @Test
    public void givenValidProfile_whenMain_thenSyncIsPerformed() throws IOException {
        // GIVEN
        String[] args = {"testProfile"};
        Profile mockProfile = new Profile("testProfile", "/tmp/A", "/tmp/B");
        Registry mockRegistry = new Registry("testProfile");

        FileComponent mockArboA = mock(FileComponent.class);
        FileComponent mockArboB = mock(FileComponent.class);

        try (
            MockedStatic<ProfileManager> pmStatic = Mockito.mockStatic(ProfileManager.class);
            MockedStatic<RegistryManager> rmStatic = Mockito.mockStatic(RegistryManager.class);
            MockedStatic<FileHandlerFactory> fhfStatic = Mockito.mockStatic(FileHandlerFactory.class);
            MockedStatic<FileSystemExplorer> fsExplorerMock = Mockito.mockStatic(FileSystemExplorer.class)
        ) {
            ProfileManager mockPM = mock(ProfileManager.class);
            RegistryManager mockRM = mock(RegistryManager.class);
            FileHandler mockFH = mock(FileHandler.class);

            pmStatic.when(ProfileManager::getInstance).thenReturn(mockPM);
            rmStatic.when(RegistryManager::getInstance).thenReturn(mockRM);
            fhfStatic.when(FileHandlerFactory::createLocalFileHandler).thenReturn(mockFH);

            when(mockPM.loadProfile("testProfile")).thenReturn(mockProfile);
            when(mockRM.loadRegistry("testProfile")).thenReturn(mockRegistry);
            fsExplorerMock.when(() -> FileSystemExplorer.explore(Path.of("/tmp/A"))).thenReturn(mockArboA);
            fsExplorerMock.when(() -> FileSystemExplorer.explore(Path.of("/tmp/B"))).thenReturn(mockArboB);

            // On mock aussi accept pour éviter NullPointer
            doNothing().when(mockArboA).accept(any(FileVisitor.class));
            doNothing().when(mockArboB).accept(any(FileVisitor.class));

            // WHEN
            SyncApp.main(args);

            // THEN
            verify(mockRM).saveRegistry(mockRegistry);
            assertTrue(out.toString().contains("Synchronisation bidirectionnelle terminée"));
        }
    }

    @Test
    public void givenIOException_whenMain_thenErrorIsPrinted() throws IOException {
        // GIVEN
        String[] args = {"testProfile"};

        try (MockedStatic<ProfileManager> pmStatic = Mockito.mockStatic(ProfileManager.class)) {
            ProfileManager mockPM = mock(ProfileManager.class);
            pmStatic.when(ProfileManager::getInstance).thenReturn(mockPM);

            when(mockPM.loadProfile("testProfile")).thenThrow(new IOException("File not found"));

            // WHEN
            SyncApp.main(args);

            // THEN
            String errorOutput = err.toString();
            assertTrue(errorOutput.contains("Erreur d'E/S") || errorOutput.contains("Erreur"),
                    "Doit afficher une erreur en cas d'IOException");
        }
    }
}