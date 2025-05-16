package model;

import org.junit.jupiter.api.Test;
import visitor.FileVisitor;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DirectoryCompositeTest {

    @Test
    public void givenValidPath_whenConstructed_thenGetPathReturnsIt() {
        // GIVEN
        Path path = Path.of("/tmp/test");

        // WHEN
        DirectoryComposite dir = new DirectoryComposite(path);

        // THEN
        assertEquals(path, dir.getPath());
    }

    @Test
    public void givenChildComponents_whenAdded_thenChildrenAreStored() {
        // GIVEN
        DirectoryComposite dir = new DirectoryComposite(Path.of("/tmp"));

        FileComponent mockFile1 = mock(FileComponent.class);
        FileComponent mockFile2 = mock(FileComponent.class);

        // WHEN
        dir.add(mockFile1);
        dir.add(mockFile2);

        // THEN
        List<FileComponent> children = dir.getChildren();
        assertEquals(2, children.size());
        assertTrue(children.contains(mockFile1));
        assertTrue(children.contains(mockFile2));
    }

    @Test
    public void givenDirectoryPath_whenGetNameCalled_thenReturnsLastPathElement() {
        // GIVEN
        Path path = Path.of("/tmp/sync-project");
        DirectoryComposite dir = new DirectoryComposite(path);

        // THEN
        assertEquals("sync-project", dir.getName());
    }
}