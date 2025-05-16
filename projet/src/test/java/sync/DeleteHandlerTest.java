package sync;

import model.Registry;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteHandlerTest {

    private DeleteHandler deleteHandler;
    private Registry registry;

    @TempDir
    Path tempDir;

    Path fileA;
    Path fileB;

    @BeforeEach
    void setup() throws IOException {
        deleteHandler = new DeleteHandler();
        registry = new Registry("test");

        fileA = tempDir.resolve("fileA.txt");
        fileB = tempDir.resolve("fileB.txt");

        // Création des deux fichiers
        Files.writeString(fileA, "contenu A");
        Files.writeString(fileB, "contenu B");
    }

    @Test
    void givenDeletedInA_whenHandle_thenDeletedInBAndRegistryUpdated() throws IOException {
        // GIVEN
        Files.delete(fileA); // simulate deleted in A
        registry.put("fileA.txt", System.currentTimeMillis());

        SyncContext context = new SyncContext(fileA, fileB, "fileA.txt", registry);

        // WHEN
        deleteHandler.handle(context);

        // THEN
        assertFalse(Files.exists(fileB), "Le fichier B doit avoir été supprimé.");
        assertFalse(registry.contains("fileA.txt"), "L'entrée doit être supprimée du registre.");
    }

    @Test
    void givenDeletedInB_whenHandle_thenDeletedInAAndRegistryUpdated() throws IOException {
        // GIVEN
        Files.delete(fileB); // simulate deleted in B
        registry.put("fileA.txt", System.currentTimeMillis());

        SyncContext context = new SyncContext(fileA, fileB, "fileA.txt", registry);

        // WHEN
        deleteHandler.handle(context);

        // THEN
        assertFalse(Files.exists(fileA), "Le fichier A doit avoir été supprimé.");
        assertFalse(registry.contains("fileA.txt"), "L'entrée doit être supprimée du registre.");
    }

    @Test
    void givenFileNotInRegistry_whenHandle_thenIgnored() {
        // GIVEN : les fichiers existent mais ne sont pas enregistrés
        SyncContext context = new SyncContext(fileA, fileB, "fileA.txt", registry);

        // WHEN
        assertDoesNotThrow(() -> deleteHandler.handle(context));

        // THEN
        assertTrue(Files.exists(fileA), "Le fichier A doit être conservé.");
        assertTrue(Files.exists(fileB), "Le fichier B doit être conservé.");
    }

    @Test
    void givenFilesExistInBothSides_whenHandle_thenNothingIsDeleted() {
        // GIVEN
        registry.put("fileA.txt", System.currentTimeMillis());

        SyncContext context = new SyncContext(fileA, fileB, "fileA.txt", registry);

        // WHEN
        deleteHandler.handle(context);

        // THEN
        assertTrue(Files.exists(fileA), "Le fichier A doit être conservé.");
        assertTrue(Files.exists(fileB), "Le fichier B doit être conservé.");
        assertTrue(registry.contains("fileA.txt"), "Le registre ne doit pas être modifié.");
    }
}