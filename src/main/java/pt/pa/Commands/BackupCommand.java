package pt.pa.Commands;

import pt.pa.Backup.FileBackupHandler;
import pt.pa.Exceptions.NoMementoException;
import pt.pa.PFS;

import java.io.IOException;

/**
 * The {@code BackupCommand} class is responsible for executing and unexecuting backup operations.
 * It uses a {@link PFS} instance to manage the state and a {@link FileBackupHandler} for file-related operations.
 */
public class BackupCommand implements Command {

    private final PFS pfs;

    /**
     * Constructs a {@code BackupCommand} with the specified {@link PFS} instance.
     *
     * @param pfs The PFS instance to be used for backup operations.
     */
    public BackupCommand(PFS pfs) {
        this.pfs = pfs;
    }

    /**
     * Executes the backup operation by saving the current state of the PFS and writing it to a file.
     *
     * @throws IOException If an I/O error occurs during the execution.
     */
    @Override
    public void execute() throws IOException {
        FileBackupHandler.saveToFile(pfs.saveState());
    }

    /**
     * Unexecutes the backup operation by restoring the PFS state from the last saved memento.
     * This operation is not implemented and will throw {@link UnsupportedOperationException}.
     *
     * @throws IOException If an I/O error occurs during the unexecution.
     */
    @Override
    public void unexecute() throws IOException {
        throw new UnsupportedOperationException("Unexecute operation not supported for BackupCommand.");
    }
}
