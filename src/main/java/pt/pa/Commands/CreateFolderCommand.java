package pt.pa.Commands;

import pt.pa.Document;
import pt.pa.PFS;
import pt.pa.adts.Position;

import java.io.IOException;

/**
 * The CreateFolderCommand class represents a command to create a new folder in the file system.
 * It implements the Command interface, providing methods to execute and unexecute the creation operation.
 *
 * This command allows creating a new folder and inserting it as a child of the specified parent position in the file system.
 * After a successful execution, the file system's structure is updated accordingly. The unexecute operation removes
 * the created folder, reverting the changes made during execution.
 *
 * @author Your Name
 * @version 1.0
 * @since yyyy-mm-dd
 */
public class CreateFolderCommand implements Command {
    private final PFS pfs;
    private final Position<Document> parent;
    private final String name;
    private Position<Document> newPosition;

    /**
     * Constructs a CreateFolderCommand with the specified file system, parent position, and folder name.
     *
     * @param pfs    The file system where the folder will be created.
     * @param parent The parent position where the folder will be inserted.
     * @param name   The name of the new folder.
     */
    public CreateFolderCommand(PFS pfs, Position<Document> parent, String name) {
        this.pfs = pfs;
        this.parent = parent;
        this.name = name;
    }

    /**
     * Executes the create command, inserting the folder as a child of the specified parent position in the file system.
     *
     * @throws IOException If an I/O error occurs during the execution.
     */
    @Override
    public void execute() throws IOException {
        this.newPosition = pfs.createFolder(name, parent);
    }

    /**
     * Unexecutes the create command, removing the folder from the file system.
     *
     * @throws IOException If an I/O error occurs during the unexecution.
     */
    @Override
    public void unexecute() throws IOException {
        pfs.remove(newPosition);
    }

    /**
     * Gets the position of the newly created folder.
     *
     * @return The position of the newly created folder.
     */
    public Position<Document> getNewPosition() {
        return newPosition;
    }

    /**
     * Returns a string representation of this CreateFolderCommand.
     * The string contains the class name, the type of document created (folder), its name, and the parent's name.
     *
     * @return A string representation of this CreateFolderCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + "Folder " + newPosition.element().getName() + " created in " + parent.element().getName();
    }
}
