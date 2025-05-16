package app;

import model.Profile;
import manager.ProfileManager;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NewProfileAppTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void redirectStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void givenInvalidArgs_whenMainCalled_thenPrintUsage() {
        // GIVEN
        String[] args = {"only-one-arg"};

        // WHEN
        NewProfileApp.main(args);

        // THEN
        String output = outContent.toString();
        assertTrue(output.contains("Usage:"), "Devrait afficher le message d’usage");
    }

    @Test
    void givenValidArgs_whenMainCalled_thenProfileIsSaved() throws Exception {
        // GIVEN
        String[] args = {"testProfile", "/tmp/test-A", "/tmp/test-B"};

        try (MockedStatic<ProfileManager> mockStatic = Mockito.mockStatic(ProfileManager.class)) {
            ProfileManager mockManager = mock(ProfileManager.class);
            mockStatic.when(ProfileManager::getInstance).thenReturn(mockManager);

            // WHEN
            NewProfileApp.main(args);

            // THEN
            verify(mockManager).saveProfile(any(Profile.class));
            String output = outContent.toString();
            assertTrue(output.contains("Profil créé avec succès"), "Devrait confirmer la création");
        }
    }

    @Test
    void givenExceptionDuringSave_whenMainCalled_thenPrintError() throws Exception {
        // GIVEN
        String[] args = {"testProfile", "/tmp/test-A", "/tmp/test-B"};

        try (MockedStatic<ProfileManager> mockStatic = Mockito.mockStatic(ProfileManager.class)) {
            ProfileManager mockManager = mock(ProfileManager.class);
            doThrow(new RuntimeException("Simulated error")).when(mockManager).saveProfile(any());

            mockStatic.when(ProfileManager::getInstance).thenReturn(mockManager);

            // WHEN
            NewProfileApp.main(args);

            // THEN
            String error = errContent.toString();
            assertTrue(error.contains("Erreur lors de la création du profil"), "Devrait afficher un message d’erreur");
            assertTrue(error.contains("Simulated error"), "Le message d’erreur doit inclure l’exception");
        }
    }
}
