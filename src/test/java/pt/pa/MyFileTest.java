/**
 * JUnit test class for the MyFile class.
 */
package pt.pa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the {@link pt.pa.MyFile} class.
 */
class MyFileTest {

    private MyFile file; // The MyFile instance under test

    /**
     * Set up the test environment by creating an instance of MyFile.
     */
    @BeforeEach
    void setUp() {
        file = new MyFile("test", ".zip");
    }

    /**
     * Test for retrieving the content of the file.
     */
    @Test
    void getContent() {
        assertEquals("", file.getContent());
    }

    /**
     * Test for setting the content of the file.
     */
    @Test
    void setContent() {
        file.setContent("testeSetContent");
        assertEquals("testeSetContent", file.getContent());
    }

    /**
     * Test for checking if the file is unlocked by default.
     */
    @Test
    void getIsUnlocked() {
        assertTrue(file.getIsUnlocked());
    }

    /**
     * Test for setting the unlocked status of the file.
     */
    @Test
    void setUnlocked() {
        file.setUnlocked(false);
        assertFalse(file.getIsUnlocked());
    }

    /**
     * Test for the toString method of the file.
     */
    @Test
    void testToString() {
        assertEquals(" test", file.toString());
    }

    /**
     * Test for renaming the file.
     */
    @Test
    void testRename() {
        file.rename("NewName");
        assertEquals("NewName", file.getName());
    }
}
