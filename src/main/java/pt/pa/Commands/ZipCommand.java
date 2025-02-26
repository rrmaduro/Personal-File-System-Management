/**
 * The ZipCommand class represents a command for zipping and unzipping operations on documents within a PFS (Persistent File System).
 * It implements the Command interface and works with a Zipper object and a Position object.
 *
 * @author YourName
 * @version 1.1
 * @since 2024-01-07
 */
package pt.pa.Commands;

import pt.pa.*;
import pt.pa.adts.Position;

import java.io.IOException;

public class ZipCommand implements Command {
    private final PFS pfs;
    private final Zipper zipper;
    private final Position<Document> targetPosition;

    /**
     * Constructs a ZipCommand with the specified PFS, Zipper, and target position.
     *
     * @param pfs            The PFS (Persistent File System) object associated with the command.
     * @param zipper         The Zipper object used for zipping and unzipping operations.
     * @param targetPosition The position of the target Document to be zipped or unzipped.
     */
    public ZipCommand(PFS pfs, Zipper zipper, Position<Document> targetPosition) {
        this.pfs = pfs;
        this.zipper = zipper;
        this.targetPosition = targetPosition;
    }

    /**
     * Executes the zip operation based on the type of the target Document (Folder or MyFile).
     *
     * @throws IOException if an I/O error occurs during the execution.
     */
    @Override
    public void execute() throws IOException {
        if (targetPosition.element().isAccessable()) {
            if (targetPosition.element() instanceof Folder) {
                zipper.zipFolder(targetPosition);
            } else if (targetPosition.element() instanceof MyFile) {
                zipper.zipFile(targetPosition);
            } else {
                throw new IOException("Invalid position");
            }
        }
    }

    /**
     * Reverts the zip operation based on the type of the target Document (Folder or MyFile).
     * This method restores the original state by replacing the document at the target position with itself.
     *
     * @throws IOException if an I/O error occurs during the unexecution.
     */
    @Override
    public void unexecute() throws IOException {
        if (!targetPosition.element().isAccessable()) {
            if (targetPosition.element() instanceof Folder) {
                zipper.unzipFolder(targetPosition);
            } else if (targetPosition.element() instanceof MyFile) {
                zipper.unzipFile(targetPosition);
            } else {
                throw new IOException("Invalid position");
            }
        }
    }

    /**
     * Returns a string representation of this ZipCommand.
     * The string contains the class name, the name of the zipped/unzipped document, and the operation performed.
     *
     * @return A string representation of this ZipCommand.
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " - " + targetPosition.element().getName() + " was zipped";
    }
}
