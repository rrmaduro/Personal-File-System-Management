package pt.pa.Commands;

import pt.pa.Document;
import pt.pa.MyFile;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * The EditCommand class represents a command to edit the content of a document in a file system.
 * It implements the Command interface and allows executing and undoing the edit operation.
 * <p>This command is specifically designed for editing documents, where the document can be either a
 * file or a folder in a file system. The command saves the old content before execution, allowing
 * the undo operation to revert the content to its previous state.</p>
 *
 * @author [Author's Name]
 * @version 1.0
 * @see Command
 * @see PFS
 * @see Position
 * @see Document
 */
public class EditCommand implements Command {
    private final PFS pfs;
    private Position<Document> document;
    private String content, oldContent;

    /**
     * Constructs an EditCommand with the specified parameters.
     *
     * @param pfs        The file system (PFS) where the edit operation will be performed.
     * @param document   The position of the document to be edited.
     * @param newContent The new content for the document.
     */
    public EditCommand(PFS pfs, Position<Document> document, String newContent) {
        this.pfs = pfs;
        this.document = document;
        this.content = newContent;

        // Save the old content before execution
        if (document.element() instanceof MyFile myFile) {
            this.oldContent = myFile.getContent();
        } else {
            this.oldContent = "";
        }
    }

    /**
     * Executes the edit operation by updating the content of the specified document with the new content.
     *
     * @throws IOException If an I/O error occurs during the edit operation.
     */
    @Override
    public void execute() throws IOException {
        pfs.edit(document, content);
    }

    /**
     * Undoes the edit operation by reverting the content of the specified document to its previous state.
     *
     * @throws IOException If an I/O error occurs during the undo operation.
     */
    @Override
    public void unexecute() throws IOException {
        pfs.edit(document, oldContent);
    }

    /**
     * Returns a string representation of this EditCommand.
     * The string contains the class name, the name of the document, and the new content applied.
     *
     * @return A string representation of this EditCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + document.element().getName() + " content changed to: " + content;
    }
}
