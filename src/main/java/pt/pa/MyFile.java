package pt.pa;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * The MyFile class represents a specific type of document in a file system, which
 * is a file containing content. It extends the Document class and adds
 * functionality related to file content and locking/unlocking.
 */
public class MyFile extends Document implements Copyable {

    private final String extension;
    private boolean isUnlocked;
    private int changes;
    private long lastChangeDate;
    private String content;
    private boolean isAccessable;
    private long sizeInBytes;

    /**
     * Constructs a MyFile with the specified name and extension.
     *
     * @param name      The name of the file.
     * @param extension The file extension.
     */
    public MyFile(String name, String extension) {
        super(name);
        this.content = "";
        this.isUnlocked = true;
        this.extension = extension;
        this.isAccessable = true;
        sizeInBytes = 0;
        this.changes = 0;
        this.lastChangeDate = Instant.now().getEpochSecond();
    }

    /**
     * Retrieves the content of the file.
     *
     * @return The content of the file.
     */
    public String getContent() {
        return content;
    }
    /**
     * Modifies the content of the file.
     *
     * @param content The new content for the file.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retrieves the file extension enum.
     *
     * @return The file extension enum.
     */
    public FileExtensions getExtension() {
        for (FileExtensions ext : FileExtensions.values()) {
            if (ext.getExtension().equalsIgnoreCase(this.extension)) {
                return ext;
            }
        }
        return null;
    }

    /**
     * Retrieves the lock status of the file.
     *
     * @return True if the file is unlocked, false if it is locked.
     */
    public boolean getIsUnlocked() {
        return isUnlocked;
    }

    /**
     * Modifies the lock status of the file.
     *
     * @param unlocked True to unlock the file, false to lock it.
     */
    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }

    /**
     * Sets the accessibility status of the file.
     *
     * @param accessable True to make the file accessible, false otherwise.
     */
    public void setAccess(boolean accessable) {
        isAccessable = accessable;
    }

    /**
     * Checks if the file is accessible.
     *
     * @return True if the file is accessible, false otherwise.
     */
    public boolean isAccessable() {
        return isAccessable;
    }

    /**
     * Calculates and returns the size of the file in bytes.
     *
     * @return The size of the file in bytes.
     */
    public long getSizeInBytes() {
        return sizeInBytes = content.getBytes(StandardCharsets.UTF_8).length;
    }

    /**
     * Provides a string representation of the file.
     * <p>
     * The string representation includes the name of the file and its extension.
     *
     * @return A string representation of the file.
     */
    @Override
    public String toString() {
        return super.toString() + extension;
    }

    /**
     * Renames the file with a new name.
     * <p>
     * This method allows changing the name of the file to the specified
     * new name. Additionally, it renames the content of the file to match
     * the new name.
     *
     * @param name The new name for the file.
     */
    @Override
    public void rename(String name) {
        super.rename(name);
    }

    /**
     * Creates a copy of the file.
     * <p>
     * This method creates a new MyFile with a name derived from the original
     * file's name and appends "_copy".
     *
     * @return A copy of the file.
     */
    @Override
    public Document copy() {
        MyFile copy = new MyFile(this.getName() + "_copy", this.extension);
        copy.setContent(this.getContent());

        return copy;
    }

    /**
     * Enum representing common file extensions.
     */
    public enum FileExtensions {
        TXT(".txt"),
        DOC(".doc"),
        PDF(".pdf"),
        JPG(".jpg"),
        PNG(".png"),
        GIF(".gif"),
        HTML(".html"),
        XML(".xml"),
        CSV(".csv"),
        ZIP(".zip"),
        MP3(".mp3"),
        MP4(".mp4");

        private final String extension;

        /**
         * Constructs a FileExtensions enum value with the specified extension.
         *
         * @param extension The file extension.
         */
        FileExtensions(String extension) {
            this.extension = extension;
        }

        /**
         * Retrieves the file extension as a string.
         *
         * @return The file extension.
         */
        public String getExtension() {
            return extension;
        }

        /**
         * Provides a string representation of the file extension.
         *
         * @return A string representation of the file extension.
         */
        @Override
        public String toString() {
            return extension;
        }
    }

    /**
     * Retrieves the number of changes made to this file.
     *
     * @return The number of changes.
     */
    public int getChanges() {
        return changes;
    }

    /**
     * Increments the number of changes and updates the date of the last change.
     */
    public void incrementChanges() {
        changes++;
        lastChangeDate = Instant.now().getEpochSecond();
    }

    /**
     * Retrieves the date of the last change in this file.
     *
     * @return The date of the last change.
     */
    public long getLastChangeDate() {
        return lastChangeDate;
    }

}
