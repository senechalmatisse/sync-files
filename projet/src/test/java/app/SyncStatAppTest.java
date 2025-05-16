package app;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import stat.SyncStatFacade;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour {@link SyncStatApp}.
 */
class SyncStatAppTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void givenNoArgs_whenMain_thenPrintsUsage() {
        // GIVEN
        String[] args = {};

        // WHEN
        SyncStatApp.main(args);

        // THEN
        String output = outContent.toString();
        assertTrue(output.contains("Usage"), "Doit afficher un message d’usage si aucun argument n’est fourni.");
    }

    @Test
    void givenProfileName_whenMain_thenCallsFacade() {
        // GIVEN
        String[] args = {"testProfile"};

        try (MockedStatic<SyncStatFacade> mockedFacade = mockStatic(SyncStatFacade.class)) {
            // WHEN
            SyncStatApp.main(args);

            // THEN
            mockedFacade.verify(() -> SyncStatFacade.printStatus("testProfile"), times(1));
        }
    }
}
