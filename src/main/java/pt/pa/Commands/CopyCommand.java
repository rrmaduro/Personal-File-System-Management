package pt.pa.Commands;

import pt.pa.Document;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * The CopyCommand class represents a command to copy a document within a PFS (Persistent File System).
 * Implements the Command interface.
 * <p>
 * This command allows copying a document (either a file or a folder) from the specified source position
 * to the specified destination position in the file system. The copy operation is performed by the
 * underlying file system tree. After a successful copy, the file system's structure is updated accordingly.
 *
 * @author Your Name
 * @version 1.0
 * @since yyyy-mm-dd
 */
public class CopyCommand implements Command {
    private final PFS pfs;
    private final Position<Document> source;

    /**
     * Constructs a CopyCommand with the specified PFS and source position.
     *
     * @param pfs    The Persistent File System where the document will be copied.
     * @param source The position of the document to be copied.
     */
    public CopyCommand(PFS pfs, Position<Document> source) {
        this.pfs = pfs;
        this.source = source;
    }

    /**
     * Executes the copy operation on the document within the PFS.
     *
     * @throws IOException If an I/O error occurs during the copy operation.
     */
    @Override
    public void execute() throws IOException {
        Position<Document> copiedDocument = pfs.copy(source);
        updateClipboard(copiedDocument);
    }

    /**
     * Reverts the copy operation by removing the document from its copied position within the PFS.
     * This method removes the document that was copied during the execute operation.
     */
    @Override
    public void unexecute() {
        updateClipboard(null);
    }

    /**
     * Updates the content of the clipboard in the PFS with the specified copied document.
     *
     * @param copiedDocument The document that has been copied.
     */
    private void updateClipboard(Position<Document> copiedDocument) {
        pfs.getClipboard().setContent(copiedDocument);
    }

    /**
     * Returns a string representation of this CopyCommand.
     * The string contains the class name, the name of the copied document, and the action performed.
     *
     * @return A string representation of this CopyCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + source.element().getName() + " copied to Clipboard";
    }

}
