package pt.pa.Commands;

import pt.pa.Document;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * A command representing the paste operation in a file system.
 * It pastes a document from the clipboard to a specified destination position.
 *
 * @author [Your Name]
 * @version 1.0
 */
public class PasteCommand implements Command {
    private PFS pfs;
    private Position<Document> destination;
    private Position<Document> pastedDocument;

    /**
     * Constructs a PasteCommand with the specified persistent file system and destination position.
     *
     * @param pfs         The persistent file system.
     * @param destination The destination position for the paste operation.
     */
    public PasteCommand(PFS pfs, Position<Document> destination) {
        this.pfs = pfs;
        this.destination = destination;
    }

    /**
     * Executes the paste operation, pasting the content from the clipboard to the specified destination.
     *
     * @throws IOException If an I/O error occurs during the paste operation.
     */
    @Override
    public void execute() throws IOException {
        if (pfs.getClipboard().getContent() != null) {
            pastedDocument = pfs.paste(destination);
        } else {
            System.err.println("Nothing to paste");
        }
    }

    /**
     * Undoes the previous paste operation by removing the pasted document from the file system.
     *
     * @throws IOException If an I/O error occurs during the undo operation.
     */
    @Override
    public void unexecute() throws IOException {
        if (pastedDocument != null) {
            pfs.remove(pastedDocument);
            System.out.println("Undoing paste: Removing '" + pastedDocument.element().getName() +
                    "' from '" + destination.element().getName() + "'.");
        } else {
            System.err.println("No previous paste operation to undo.");
        }
    }

    /**
     * Returns a string representation of this PasteCommand.
     * The string contains the class name, the name of the pasted document, and the destination position.
     *
     * @return A string representation of this PasteCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + pfs.getClipboard().getContent().element().getName() + " pasted to: " + destination.element().getName();
    }
}

