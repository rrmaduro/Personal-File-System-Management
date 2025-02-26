/**
 * JUnit test class for the Folder class.
 */
package pt.pa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the {@link pt.pa.Folder} class.
 */
class FolderTest {

    private Folder folder; // The folder instance under test
    private MyFile file1; // First test file
    private MyFile file2; // Second test file

    /**
     * Set up the test environment by creating instances of Folder and MyFile.
     */
    @BeforeEach
    void setUp() {
        folder = new Folder("folderTest");
        file1 = new MyFile("testFile1", ".mp4");
        file2 = new MyFile("testFile2",".mp3");
    }

    /**
     * Test for the toString method of the folder.
     */
    @Test
    void testToString() {
        assertEquals(" folderTest", folder.toString());
    }

    /**
     * Test for renaming the folder.
     */
    @Test
    void testRename() {
        folder.rename("folderTest");
        assertEquals("folderTest", folder.getName());
    }

}
