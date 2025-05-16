package sync;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import model.Registry;

/**
 * Test unitaire pour la classe {@link AbstractSyncHandler}.
 * Vérifie le chaînage et la délégation du traitement à un maillon suivant.
 */
public class AbstractSyncHandlerTest {

    @Test
    public void givenHandlerWithNext_whenHandleIsCalled_thenNextHandlerIsCalled() {
        // GIVEN : un handler avec un maillon suivant simulé
        AtomicBoolean nextWasCalled = new AtomicBoolean(false);
        AbstractSyncHandler firstHandler = new AbstractSyncHandler() {};
        SyncHandler nextHandler = new SyncHandler() {
            @Override
            public void setNext(SyncHandler next) {
                // inutile pour ce test
            }
    
            @Override
            public void handle(SyncContext context) {
                nextWasCalled.set(true);
            }
        };
        firstHandler.setNext(nextHandler);
    
        SyncContext context = new SyncContext(
                Path.of("A/file.txt"),
                Path.of("B/file.txt"),
                "file.txt",
                new Registry("testProfile")
        );
    
        // WHEN
        firstHandler.handle(context);
    
        // THEN
        assertTrue(nextWasCalled.get(), "Le maillon suivant n’a pas été appelé.");
    }

    @Test
    public void givenSetNext_whenCalled_thenNextHandlerIsStored() {
        // GIVEN : un handler et un handler suivant fictif
        AbstractSyncHandler handler = new AbstractSyncHandler() {};
        SyncHandler nextHandler = new SyncHandler() {
            @Override
            public void setNext(SyncHandler next) {
                // rien ici
            }
    
            @Override
            public void handle(SyncContext context) {
                // rien ici
            }
        };
    
        handler.setNext(nextHandler);
    
        SyncContext context = new SyncContext(
                Path.of("A/file.txt"),
                Path.of("B/file.txt"),
                "file.txt",
                new Registry("testProfile")
        );
    
        // WHEN + THEN
        assertDoesNotThrow(() -> handler.handle(context),
                "Le handler doit pouvoir déléguer au prochain sans lever d'exception.");
    }

    @Test
    public void givenNoNextHandler_whenHandleIsCalled_thenNothingHappens() {
        // GIVEN : un AbstractSyncHandler sans maillon suivant
        AbstractSyncHandler handler = new AbstractSyncHandler() {};

        SyncContext context = new SyncContext(
                Path.of("A/file.txt"),
                Path.of("B/file.txt"),
                "file.txt",
                new Registry("testProfile")
        );

        // WHEN + THEN : aucun comportement attendu mais pas d'exception
        assertDoesNotThrow(() -> handler.handle(context),
                "Le handler sans maillon suivant ne doit pas lever d'exception.");
    }
}