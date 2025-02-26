package pt.pa.Backup;

import pt.pa.Exceptions.NoMementoException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The FileBackupHandler class is responsible for handling file-related operations for PFSCaretaker.
 */
public class FileBackupHandler {

    private static final String BACKUP_FOLDER = "src/main/java/pt/pa/Backup/Backups/";

    static {
        File backupDir = new File(BACKUP_FOLDER);
        backupDir.mkdirs();
    }

    private FileBackupHandler() {
    }

    /**
     * Saves the memento to a file in the "Backups" folder.
     *
     * @param memento The memento to be saved.
     */
    public static void saveToFile(Memento memento) {
        String fileName = generateBackupFileName();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BACKUP_FOLDER + fileName))) {
            oos.writeObject(memento);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Restores the memento from a file in the "Backups" folder.
     *
     * @param fileName The name of the file containing the backup information.
     * @return The restored memento.
     * @throws NoMementoException If there is no memento to restore.
     */
    public static Memento restoreFromFile(String fileName) throws NoMementoException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BACKUP_FOLDER + fileName))) {
            return (Memento) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NoMementoException();
        }
    }

    /**
     * Generates a backup filename using the pattern "pfsBackup_hour".
     *
     * @return The generated backup filename.
     */
    private static String generateBackupFileName() {
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd__HH-mm");
        return "pfsBackup_" + currentTime.format(formatter) + ".bak";
    }
}
