package manager;

import model.Registry;
import org.junit.jupiter.api.*;

import factory.XmlRegistryStrategyFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RegistryManagerTest {

    private final String profileName = "unitTestRegistry";
    private final Path testFilePath = Paths.get("registries", "registry_" + profileName + ".json");

    private RegistryManager registryManager;

    @BeforeAll
    void initManager() {
        RegistryManager.init(new XmlRegistryStrategyFactory().createStrategy());
        registryManager = RegistryManager.getInstance();
    }

    @BeforeEach
    void cleanBeforeEach() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    @AfterAll
    void cleanAfterAll() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    @Test
    void shouldBeSingleton() {
        RegistryManager a = RegistryManager.getInstance();
        RegistryManager b = RegistryManager.getInstance();
        assertSame(a, b, "RegistryManager doit être un singleton");
    }

    @Test
    void shouldLoadRegistryFromFile() throws IOException {
        // GIVEN
        Registry original = new Registry(profileName);
        original.put("some/file.txt", 987654321L);
        registryManager.saveRegistry(original);

        // WHEN
        Registry loaded = registryManager.loadRegistry(profileName);

        // THEN
        assertNotNull(loaded);
        assertEquals(profileName, loaded.getProfileName());
        assertEquals(987654321L, loaded.get("some/file.txt"));
    }

    @Test
    void shouldReturnEmptyRegistryIfFileDoesNotExist() throws IOException {
        // GIVEN: le fichier est supprimé
        Files.deleteIfExists(testFilePath);

        // WHEN
        Registry reg = registryManager.loadRegistry(profileName);

        // THEN
        assertNotNull(reg);
        assertEquals(profileName, reg.getProfileName());
        assertTrue(reg.getEntries().isEmpty(), "Le registre doit être vide s'il n'existe pas encore");
    }

    @Test
    void givenRegistryWithEntries_whenPrintRegistry_thenDisplaysFormattedEntries() throws IOException {
        // GIVEN
        Registry registry = new Registry(profileName);
        registry.put("fileA.txt", 1700000000000L); // timestamp fixe

        // Sauvegarder le registre
        registryManager.saveRegistry(registry);

        // Rediriger la sortie standard
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // WHEN
            registryManager.printRegistry(profileName);

            // THEN
            String output = outContent.toString();
            assertTrue(output.contains("fileA.txt"));
            assertTrue(output.contains("→")); // Vérifie que la date est formatée
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void givenEmptyRegistry_whenPrintRegistry_thenDisplaysEmptyMessage() throws IOException {
        // GIVEN
        Registry empty = new Registry(profileName);
        registryManager.saveRegistry(empty);

        PrintStream originalOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        try {
            // WHEN
            registryManager.printRegistry(profileName);

            // THEN
            String result = out.toString();
            assertTrue(result.contains("(aucune entrée)"));
        } finally {
            System.setOut(originalOut);
        }
    }
}