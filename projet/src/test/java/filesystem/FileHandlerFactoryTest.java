package filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileHandlerFactoryTest {
    
    @Test
    void shouldReturnLocalFileHandlerInstance() {
        FileHandler handler = FileHandlerFactory.createLocalFileHandler();
        assertNotNull(handler);
        assertTrue(handler instanceof LocalFileHandler);
    }
}
