package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProfileTest {

    @Test
    @DisplayName("GIVEN valid parameters WHEN creating Profile THEN values should be stored correctly")
    void givenValidParameters_whenCreatingProfile_thenStoresValuesCorrectly() {
        // GIVEN
        String name = "testProfile";
        String pathA = "/tmp/test-A";
        String pathB = "/tmp/test-B";

        // WHEN
        Profile profile = new Profile(name, pathA, pathB);

        // THEN
        assertEquals(name, profile.getName());
        assertEquals(pathA, profile.getPathA());
        assertEquals(pathB, profile.getPathB());
    }

    @Test
    @DisplayName("GIVEN null name WHEN creating Profile THEN throws IllegalArgumentException")
    void givenNullName_whenCreatingProfile_thenThrowsException() {
        // GIVEN
        String pathA = "/tmp/test-A";
        String pathB = "/tmp/test-B";

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> new Profile(null, pathA, pathB));
    }

    @Test
    @DisplayName("GIVEN null pathA WHEN creating Profile THEN throws IllegalArgumentException")
    void givenNullPathA_whenCreatingProfile_thenThrowsException() {
        // GIVEN
        String name = "testProfile";
        String pathB = "/tmp/test-B";

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> new Profile(name, null, pathB));
    }

    @Test
    @DisplayName("GIVEN null pathB WHEN creating Profile THEN throws IllegalArgumentException")
    void givenNullPathB_whenCreatingProfile_thenThrowsException() {
        // GIVEN
        String name = "testProfile";
        String pathA = "/tmp/test-A";

        // WHEN / THEN
        assertThrows(IllegalArgumentException.class, () -> new Profile(name, pathA, null));
    }
}