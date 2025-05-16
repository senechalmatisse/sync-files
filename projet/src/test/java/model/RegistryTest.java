package model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class RegistryTest {

    private Registry registry;

    @BeforeEach
    public void setup() {
        registry = new Registry("testProfile");
    }

    @Test
    public void givenNewRegistry_whenGetProfileName_thenReturnsCorrectName() {
        // GIVEN/WHEN
        String name = registry.getProfileName();

        // THEN
        assertEquals("testProfile", name);
    }

    @Test
    public void givenEmptyRegistry_whenPut_thenEntryIsAdded() {
        // GIVEN
        String path = "doc.txt";
        long timestamp = 123456789L;

        // WHEN
        registry.put(path, timestamp);

        // THEN
        assertTrue(registry.contains(path));
        assertEquals(timestamp, registry.get(path));
    }

    @Test
    public void givenEntryExists_whenPutSameKey_thenUpdatesValue() {
        // GIVEN
        String path = "doc.txt";
        registry.put(path, 1000L);

        // WHEN
        registry.put(path, 2000L);

        // THEN
        assertEquals(2000L, registry.get(path));
    }

    @Test
    public void givenEntryExists_whenRemove_thenEntryIsGone() {
        // GIVEN
        registry.put("temp.txt", 9999L);
        assertTrue(registry.contains("temp.txt"));

        // WHEN
        registry.remove("temp.txt");

        // THEN
        assertFalse(registry.contains("temp.txt"));
        assertNull(registry.get("temp.txt"));
    }

    @Test
    public void givenNoEntry_whenGet_thenReturnsNull() {
        // WHEN
        Long value = registry.get("inexistant.txt");

        // THEN
        assertNull(value);
    }

    @Test
    public void givenNewRegistry_whenSetProfileName_thenUpdated() {
        // WHEN
        registry.setProfileName("updatedProfile");

        // THEN
        assertEquals("updatedProfile", registry.getProfileName());
    }
}