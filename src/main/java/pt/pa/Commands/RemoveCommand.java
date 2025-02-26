package pt.pa.Commands;

import pt.pa.Document;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;
/**
 * The RemoveCommand class represents a command to remove a document from a PFS (Persistent File System).
 * Implements the Command interface.
 *
 * @author Your Name
 * @version 1.0
 * @since yyyy-mm-dd
 */
public class RemoveCommand implements Command {
    private final PFS pfs;
    private Position<Document> document, oldPosition;

    /**
     * Constructs a RemoveCommand with the specified PFS and the position of the document to be removed.
     *
     * @param pfs         The Persistent File System from which the document will be removed.
     * @param newDocument The position of the document to be removed.
     */
    public RemoveCommand(PFS pfs, Position<Document> newDocument) {
        this.pfs = pfs;
        this.document = newDocument;
        this.oldPosition = pfs.getPfs().parent(newDocument);
    }

    /**
     * Executes the remove operation on the document within the PFS.
     */
    @Override
    public void execute() {
        pfs.remove(document);
    }

    /**
     * Reverts the remove operation by adding the document back to its original position within the PFS.
     * This method restores the document to its original position by adding it back as a child of its
     * original parent.
     *
     * @throws IOException If an I/O error occurs during the undo operation.
     */
    @Override
    public void unexecute() throws IOException {
        pfs.move(document, oldPosition);
    }


    /**
     * Returns a string representation of this RemoveCommand.
     * The string contains the class name, the name of the removed document, and the original position.
     *
     * @return A string representation of this RemoveCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + document.element().getName() + " removed from " + oldPosition.element().getName();
    }

}
