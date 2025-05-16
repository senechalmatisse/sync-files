package model;

import org.junit.jupiter.api.*;

import visitor.FileVisitor;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FileLeafTest {

    @Test
    public void shouldReturnFilePathAndName() {
        Path path = Path.of("/tmp/example.txt");
        FileLeaf file = new FileLeaf(path);

        assertEquals(path, file.getPath());
        assertEquals("example.txt", file.getName());
    }

    @Test
    public void shouldAcceptVisitor() {
        Path path = Path.of("/tmp/f.txt");
        FileLeaf file = new FileLeaf(path);

        final boolean[] visited = {false};
        file.accept(new FileVisitor() {
            @Override public void visit(FileComponent file) { visited[0] = true; }
        });

        assertTrue(visited[0]);
    }
}