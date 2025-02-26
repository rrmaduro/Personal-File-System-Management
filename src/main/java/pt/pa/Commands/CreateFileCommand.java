package pt.pa.Commands;

import pt.pa.Document;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * The CreateFileCommand class represents a command to create a new document (file) in the file system.
 * It implements the Command interface, providing methods to execute and unexecute the creation operation.
 * <p>
 * This command allows creating a new file and inserting it as a child of the specified parent position in the file system.
 * After a successful execution, the file system's structure is updated accordingly. The unexecute operation removes
 * the created file, reverting the changes made during execution.
 */
public class CreateFileCommand implements Command {
    private final PFS pfs;
    private final Position<Document> parent;
    private Position<Document> newPosition;
    private String name;

    /**
     * Constructs a CreateFileCommand with the specified file system, parent position, and document name.
     *
     * @param pfs    The file system where the document will be created.
     * @param parent The parent position where the document will be inserted.
     * @param name   The name of the new file.
     */
    public CreateFileCommand(PFS pfs, Position<Document> parent, String name) {
        this.pfs = pfs;
        this.parent = parent;
        this.name = name;
    }

    /**
     * Executes the create command, inserting the document as a child of the specified parent position in the file system.
     *
     * @throws IOException If an I/O error occurs during the execution.
     */
    @Override
    public void execute() throws IOException {
        this.newPosition = pfs.createFile(this.name, parent);
    }

    /**
     * Unexecutes the create command, removing the document from the file system.
     *
     * @throws IOException If an I/O error occurs during the unexecution.
     */
    @Override
    public void unexecute() throws IOException {
        pfs.remove(newPosition);
    }

    /**
     * Gets the position of the newly created document.
     *
     * @return The position of the newly created document.
     */
    public Position<Document> getNewPosition() {
        return newPosition;
    }

    /**
     * Returns a string representation of this CreateFileCommand.
     * The string contains the class name, the type of document created, its name, and the parent's name.
     *
     * @return A string representation of this CreateFileCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + "File " + newPosition.element().getName() + " created in " + parent.element().getName();
    }
}
