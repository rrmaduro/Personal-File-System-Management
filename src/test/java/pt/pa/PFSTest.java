/**
 * JUnit test class for the PFS class.
 */
package pt.pa;

import pt.pa.Exceptions.InvalidMoveException;
import pt.pa.adts.InvalidPositionException;
import pt.pa.adts.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the {@link pt.pa.PFS} class.
 */
class PFSTest {

    private PFS pfs;
    Clipboard clipboard = new Clipboard();

    /**
     * Set up the test environment by creating an instance of PFS with a root document.
     */
    @BeforeEach
    public void setUp() throws IOException {
        Document rootDocument = new MyFile("Root", ".txt");
        pfs = new PFS(rootDocument);
    }

    /**
     * Test for retrieving the children of a document in the file system.
     */
    @Test
    public void testGetChildren() {
        Position<Document> root = pfs.find("Root");
        pfs.createFile("ChildFile.txt", root);
        pfs.createFolder("ChildFolder", root);
        Iterable<Position<Document>> children = pfs.getPfs().children(root);
        int childCount = 0;
        for (Position<Document> child : children) {
            childCount++;
        }
        assertEquals(2, childCount);
    }

    /**
     * Test for finding a document by name in the file system.
     */
    @Test
    public void testFind() {
        Position<Document> root = pfs.find("Root");
        pfs.createFile("ToFind.txt", root);
        Position<Document> foundPosition = pfs.find("ToFind.txt");
        assertNotNull(foundPosition);
        assertEquals("ToFind.txt", foundPosition.element().getName());
    }

    /**
     * Test for creating a file within a specified position in the file system.
     */
    @Test
    public void testCreateFile() {
        Position<Document> root = pfs.find("Root");
        Position<Document> newFile = pfs.createFile("NewFile.txt", root);
        assertNotNull(newFile);
        assertEquals("NewFile.txt", newFile.element().getName());
    }

    /**
     * Test for creating a folder within a specified position in the file system.
     */
    @Test
    public void testCreateFolder() {
        Position<Document> root = pfs.find("Root");
        Position<Document> newFolder = pfs.createFolder("NewFolder", root);
        assertNotNull(newFolder);
        assertEquals("NewFolder", newFolder.element().getName());
    }

    /**
     * Test for renaming a document within the file system.
     */
    @Test
    public void testRename() {
        Position<Document> root = pfs.find("Root");
        Position<Document> file = pfs.createFile("OldName.txt", root);
        pfs.rename(file, "NewName.txt");
        assertEquals("NewName.txt", file.element().getName());
    }

    /**
     * Test for removing a document from the file system.
     */
    @Test
    public void testRemove() {
        Position<Document> root = pfs.find("Root");
        Position<Document> file = pfs.createFile("TestRemove", root);
        pfs.remove(file);
        assertNull(pfs.find("TestRemove"));
    }

    /**
     * Test for editing the content of a file in the file system.
     */
    @Test
    public void testEdit() {
        Position<Document> root = pfs.find("Root");
        Position<Document> file = pfs.createFile("TestEdit", root);
        pfs.edit(file, "New content");
        assertEquals("New content", ((MyFile) file.element()).getContent());
    }

    /**
     * Test for showing the content of a file in the file system.
     */
    @Test
    public void testShowFileContent() {
        Position<Document> root = pfs.find("Root");
        Position<Document> file = pfs.createFile("ShowTest", root);
        MyFile myFile = (MyFile) file.element();
        myFile.setContent("testShow.");
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        pfs.show(file);
        assertTrue(outContent.toString().contains("File Content:\ntestShow."));
    }

    @Test
    public void testCopyFolder() throws IOException, InvalidPositionException {
        Position<Document> root = pfs.find("Root");
        Position<Document> folderTest = pfs.createFolder("CopyTestFolder", root);
        Position<Document> folder = pfs.createFolder("TargetFolder", root);

        System.out.println("x");
        Position<Document> fileInSource = pfs.createFile("FileInSource.txt", folderTest);
        Position<Document> subFolderInSource = pfs.createFolder("SubFolderInSource", folderTest);

        System.out.println("x");

        System.out.println(folderTest.element().getClass().getSimpleName());

        // Copy the folder and set it in the clipboard
        Position<Document> copiedFolderPosition = pfs.copy(folderTest);
        System.out.println("x");
        clipboard.setContent(copiedFolderPosition);
        System.out.println("x");
        // Paste the copied folder into the target folder
        Position<Document> pastedFolderPosition = pfs.paste(folder);
        System.out.println("x");
        // Check if the folder is copied
        assertTrue(pfs.hasChild(folder, pastedFolderPosition.element()));
        System.out.println("x");
        // Check if the files and subfolders in the source folder are also copied
        assertTrue(pfs.hasChild(folder, fileInSource.element()));
        assertTrue(pfs.hasChild(folder, subFolderInSource.element()));
        System.out.println("x");
    }


    @Test
    public void testCopy() {
        try {
            // Create a sample PFS instance with a root document
            PFS pfs = new PFS(new Folder("root"));

            // Populate the PFS with sample folders and files
            pfs.populatePFS();

            // Find the position of a document to copy
            Position<Document> documentToCopyPosition = pfs.find("Prints");

            // Perform the copy operation
            Position<Document> copiedDocumentPosition = pfs.copy(documentToCopyPosition);

            // Verify that the copied document is not null
            assertNotNull(copiedDocumentPosition);

            // Verify that the copied document is the same as the original document
            assertSame(documentToCopyPosition, copiedDocumentPosition);

            // Verify that the parent of the copied document is the same as the original parent
            assertEquals(pfs.getPfs().parent(documentToCopyPosition), pfs.getPfs().parent(copiedDocumentPosition));

            // Optional: Verify any additional conditions based on your specific requirements

        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testPaste() throws IOException {
        // Create a sample PFS instance with a root document
        PFS pfs = new PFS(new Folder("root"));
        pfs.populatePFS();
        // Copy a folder from the file system
        Position<Document> sourceFolder = pfs.find("Pictures");
        Position<Document> copiedFolder = pfs.copy(sourceFolder);


        // Paste the copied folder to a different location
        Position<Document> destinationFolder = pfs.find("Documents");
        Position<Document> pastedFolder = pfs.paste(destinationFolder, copiedFolder);

        // Check if the pasted folder is not null and has the correct name
        assertNotNull(pastedFolder);
        assertEquals("Pictures", pastedFolder.element().getName());

        // Check if the pasted folder has the correct parent
        assertEquals(destinationFolder, pfs.getPfs().parent(pastedFolder));


    }


    /**
     * Test for copying a file within the file system.
     *
     * @throws InvalidPositionException If an invalid position is encountered during the test.
     */
    @Test
    public void testCopyFile() throws InvalidPositionException {
        Position<Document> root = pfs.find("Root");
        Position<Document> file = pfs.createFile("CopyTestFile.txt", root);
        Position<Document> folder = pfs.createFolder("TargetFolder", root);


        Position<Document> copiedFile = pfs.copy(file);
        pfs.paste(folder);

        Iterable<Position<Document>> it = pfs.getPfs().children(folder);
        AtomicBoolean test = new AtomicBoolean(false);
        it.forEach(p -> {
            if (p.element().equals(copiedFile)) {
                test.set(true);
            }
        });

        assertTrue(test.get());
    }


    /**
     * Test for moving a document to a different folder within the file system.
     *
     * @throws IOException          If an I/O error occurs during the test.
     * @throws InvalidMoveException If an invalid move is attempted during the test.
     */
    @Test
    public void testMove() throws IOException, InvalidMoveException {
        Position<Document> root = pfs.find("Root");
        Position<Document> file = pfs.createFile("MoveTest", root);
        Position<Document> folder = pfs.createFolder("TargetFolder", root);
        pfs.move(file, folder);
        assertEquals(folder, pfs.getPfs().parent(file));
    }

    /**
     * Test for obtaining a string representation of the file system.
     */
    @Test
    public void testToString() {
        assertEquals(" Root", pfs.toString());
    }
}
