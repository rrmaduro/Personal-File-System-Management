package pt.pa.Commands;

import pt.pa.Document;
import pt.pa.Exceptions.InvalidNameException;
import pt.pa.PFS;
import pt.pa.adts.Position;

/**
 * A command to rename a document within a PFS.
 * Implements the Command interface.
 *
 * @author Your Name
 * @version 1.0
 * @since yyyy-mm-dd
 */
public class RenameCommand implements Command {
    private final PFS pfs;
    private Position<Document> document;
    private String oldName, name;

    /**
     * Constructs a RenameCommand with the specified PFS, document position, and new name.
     *
     * @param pfs         Where the document is located.
     * @param newDocument The position of the document to be renamed.
     * @param name        The new name for the document.
     * @throws IllegalArgumentException If the provided PFS is null or the new document position is null.
     * @throws InvalidNameException     If the specified new name is null.
     */
    public RenameCommand(PFS pfs, Position<Document> newDocument, String name)
            throws IllegalArgumentException, InvalidNameException {
        if (pfs == null) {
            throw new IllegalArgumentException("PFS cannot be null.");
        }

        if (newDocument == null || newDocument.element() == null) {
            throw new IllegalArgumentException("Document position cannot be null.");
        }

        if (name == null) {
            throw new InvalidNameException("Invalid new name");
        }

        this.pfs = pfs;
        this.document = newDocument;
        this.name = name;
        this.oldName = newDocument.element().getName();
    }

    /**
     * Executes the rename operation on the document within the PFS.
     */
    @Override
    public void execute() {
        pfs.rename(document, name);
    }

    /**
     * Reverts the rename operation by restoring the document's previous name within the PFS.
     */
    @Override
    public void unexecute() {
        pfs.rename(document, oldName);
    }

    /**
     * Returns a string representation of this RenameCommand.
     * The string contains the class name, the name of the renamed document, and the new name.
     *
     * @return A string representation of this RenameCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + document.element().getName() + " renamed as " + name;
    }
}
