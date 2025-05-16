package stat;

import manager.*;
import model.Profile;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SyncStatFacadeTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void redirectStreams() {
        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void givenValidProfile_whenPrintStatus_thenDisplaysProfileAndRegistry() throws IOException {
        // GIVEN
        Profile mockProfile = new Profile("testProfile", "/path/A", "/path/B");

        try (
            MockedStatic<ProfileManager> profileManagerMock = mockStatic(ProfileManager.class);
            MockedStatic<RegistryManager> registryManagerMock = mockStatic(RegistryManager.class)
        ) {
            ProfileManager pm = mock(ProfileManager.class);
            when(pm.loadProfile("testProfile")).thenReturn(mockProfile);
            profileManagerMock.when(ProfileManager::getInstance).thenReturn(pm);

            RegistryManager rm = mock(RegistryManager.class);
            doAnswer(invocation -> {
                System.out.println("Contenu du registre simulé...");
                return null;
            }).when(rm).printRegistry("testProfile");
            registryManagerMock.when(RegistryManager::getInstance).thenReturn(rm);

            // WHEN
            SyncStatFacade.printStatus("testProfile");

            // THEN
            String output = out.toString();
            assertTrue(output.contains("Profil : testProfile"));
            assertTrue(output.contains("Dossier A : /path/A"));
            assertTrue(output.contains("Dossier B : /path/B"));
            assertTrue(output.contains("Contenu du registre simulé..."));
        }
    }

    @Test
    public void givenProfileLoadFails_whenPrintStatus_thenDisplaysError() throws IOException {
        // GIVEN
        try (MockedStatic<ProfileManager> profileManagerMock = mockStatic(ProfileManager.class)) {
            ProfileManager pm = mock(ProfileManager.class);
            when(pm.loadProfile("invalid")).thenThrow(new IOException("Fichier introuvable"));
            profileManagerMock.when(ProfileManager::getInstance).thenReturn(pm);

            // WHEN
            SyncStatFacade.printStatus("invalid");

            // THEN
            String errorOutput = err.toString();
            assertTrue(errorOutput.contains("Impossible de charger le profil \"invalid\""));
            assertTrue(errorOutput.contains("Fichier introuvable"));
        }
    }
}