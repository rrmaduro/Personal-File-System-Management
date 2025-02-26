package pt.pa.Backup;

import pt.pa.Exceptions.NoMementoException;
import pt.pa.PFS;

import java.util.Stack;

/**
 * The PFSCaretaker class is responsible for managing the backup states of a PFS using mementos.
 */
public class PFSCaretaker {
    private final Stack<Memento> mementos;
    private final PFS originator;

    /**
     * Constructs a PFSCaretaker with the specified PFS originator.
     *
     * @param originator The PFS originator whose states will be managed.
     */
    public PFSCaretaker(PFS originator) {
        this.mementos = new Stack<>();
        this.originator = originator;
    }

    /**
     * Saves the current state of the PFS as a memento and logs the information to a file.
     *
     */
    public void saveState() {
        Memento memento = originator.saveState();
        mementos.push(memento);

        FileBackupHandler.saveToFile(memento);
    }

    /**
     * Restores the PFS state from the last saved memento stored in a file.
     *
     * @param fileName The name of the file containing the backup information.
     * @throws NoMementoException If there is no memento to restore.
     */
    public void restoreState(String fileName) throws NoMementoException {
        Memento lastMemento = FileBackupHandler.restoreFromFile(fileName);
        mementos.push(lastMemento);
        originator.setState(lastMemento);
    }
}