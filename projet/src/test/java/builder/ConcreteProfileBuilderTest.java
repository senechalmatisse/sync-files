package builder;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.*;

public class ConcreteProfileBuilderTest {

    @Test
    void shouldBuildProfileSuccessfully() {
        ConcreteProfileBuilder builder = new ConcreteProfileBuilder();
        builder.startProfile();
        builder.setName("test");
        builder.setPathA("/path/to/A");
        builder.setPathB("/path/to/B");
        Profile profile = builder.getProfile();

        assertEquals("test", profile.getName());
        assertEquals("/path/to/A", profile.getPathA());
        assertEquals("/path/to/B", profile.getPathB());
    }

    @Test
    void shouldThrowIfMissingField() {
        ConcreteProfileBuilder builder = new ConcreteProfileBuilder();
        builder.startProfile();
        builder.setName("test");
        builder.setPathA("/path/to/A");
        // Pas de pathB
        assertThrows(IllegalStateException.class, builder::getProfile);
    }

    @Test
    void shouldThrowIfSetTwice() {
        ConcreteProfileBuilder builder = new ConcreteProfileBuilder();
        builder.startProfile();
        builder.setName("profile");
        assertThrows(IllegalStateException.class, () -> builder.setName("other"));
    }
}