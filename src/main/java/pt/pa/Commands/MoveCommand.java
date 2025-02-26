package pt.pa.Commands;

import pt.pa.Document;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * The MoveCommand class represents a command to move a document within a PFS (Persistent File System).
 * Implements the Command interface.
 * <p>
 * This command allows moving a document (either a file or a folder) from the specified initial position
 * to the specified end position in the file system. The move operation is performed by the underlying
 * file system tree. After a successful move, the file system's structure is updated accordingly.
 *
 * @author Your Name
 * @version 1.0
 * @since yyyy-mm-dd
 */
public class MoveCommand implements Command {
    private PFS pfs;
    private Position<Document> initial;
    private Position<Document> end;

    private Position<Document> oldPosition;

    /**
     * Constructs a MoveCommand with the specified PFS, initial position, and end position.
     *
     * @param pfs     The Persistent File System where the document will be moved.
     * @param initial The position of the document to be moved.
     * @param end     The position where the document will be moved.
     */
    public MoveCommand(PFS pfs, Position<Document> initial, Position<Document> end) {
        this.pfs = pfs;
        this.initial = initial;
        this.end = end;
        this.oldPosition = pfs.getPfs().parent(initial);
    }

    /**
     * Executes the move operation on the document within the PFS.
     *
     * @throws IOException If an I/O error occurs during the move operation.
     */
    @Override
    public void execute() throws IOException {
        pfs.move(initial, end);
    }

    /**
     * Reverts the move operation by moving the document back to its original position within the PFS.
     * This method restores the document to its original position by moving it back to its original parent.
     *
     * @throws IOException If an I/O error occurs during the move operation.
     */
    @Override
    public void unexecute() throws IOException {
        pfs.move(initial, oldPosition);
    }

    /**
     * Returns a string representation of this MoveCommand.
     * The string contains the class name, the name of the document being moved, and the destination position.
     *
     * @return A string representation of this MoveCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + initial.element().getName() + " moved to: " + end.element().getName();
    }
}

