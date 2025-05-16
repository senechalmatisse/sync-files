package manager;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import factory.XmlProfileStrategyFactory;
import model.Profile;

import java.io.*;
import java.nio.file.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileManagerTest {

    private final String testProfileName = "unitTestProfile";
    private final Path testFilePath = Paths.get("profiles", testProfileName + ".sync");

    @BeforeAll
    void initManager() {
        ProfileManager.init(new XmlProfileStrategyFactory().createStrategy());
    }

    @BeforeEach
    void cleanBefore() {
        try {
            Files.deleteIfExists(testFilePath);
        } catch (IOException ignored) {}
    }

    @AfterAll
    void cleanupAfterAll() {
        try {
            Files.deleteIfExists(testFilePath);
        } catch (IOException ignored) {}
    }

    @Test
    void shouldBeSingleton() {
        ProfileManager first = ProfileManager.getInstance();
        ProfileManager second = ProfileManager.getInstance();
        assertSame(first, second, "ProfileManager doit être un singleton");
    }

    @Test
    void shouldSaveProfileToFile() throws IOException {
        String testProfileName = "test-profile";
        Path expectedFile = Paths.get("profiles", testProfileName + ".xml");
    
        Profile profile = new Profile(testProfileName, "/tmp/A", "/tmp/B");
        ProfileManager.getInstance().saveProfile(profile);
    
        assertTrue(Files.exists(expectedFile));
        String content = Files.readString(expectedFile);
        assertTrue(content.contains("/tmp/A"));
        assertTrue(content.contains("/tmp/B"));
    }    

    @Test
    void shouldLoadProfileFromFile() throws IOException {
        // Given: un profil sauvegardé
        Profile original = new Profile(testProfileName, "/tmp/A", "/tmp/B");
        ProfileManager.getInstance().saveProfile(original);

        // When: on le recharge
        Profile loaded = ProfileManager.getInstance().loadProfile(testProfileName);

        // Then: les données sont identiques
        assertEquals(original.getName(), loaded.getName());
        assertEquals(original.getPathA(), loaded.getPathA());
        assertEquals(original.getPathB(), loaded.getPathB());
    }
}