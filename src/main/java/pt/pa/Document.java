package pt.pa;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * The Document class represents a basic element in a file system, encapsulating
 * information about a document, such as its name. Instances of this class can
 * be either a File or a Folder in a file system.
 * Instances of this class are meant to be used as the base class for more
 * specific types of documents in a file system.
 * This class is abstract and should be extended by
 * more specialized document types (e.g., File or Folder).
 *
 * @author [Author's Name]
 * @version 1.0
 */
public abstract class Document implements Serializable {

    /**
     * The name of the document.
     */
    private String name;
    private boolean isAccessable;
    private String content;
    private long creationDate;
    private String creationDateFormatted;

    /**
     * Constructs a Document with the specified name.
     *
     * @param name The name of the document.
     */
    public Document(String name) {
        this.name = name;
        this.isAccessable = true;
        this.creationDate = Instant.now().getEpochSecond();
        setCreationDate(creationDate);
    }

    /**
     * Retrieves the name of the document.
     *
     * @return The name of the document.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the creation date of the document.
     *
     * @return The creation date of the document.
     */
    public long getCreationDate() {
        return creationDate;
    }

    /**
     * Renames the document with a new name.
     * <p>
     * This method allows changing the name of the document to the specified
     * new name.
     *
     * @param name The new name for the document.
     */
    public void rename(String name) {
        this.name = name;
    }

    /**
     * Checks if the document is accessible.
     *
     * @return True if the document is accessible, false otherwise.
     */
    public boolean isAccessable() {
        return isAccessable;
    }

    /**
     * Sets the accessibility status of the document.
     *
     * @param accessable True to make the document accessible, false otherwise.
     */
    public void setAccess(boolean accessable) {
        isAccessable = accessable;
    }

    /**
     * Provides a string representation of the document.
     * The string representation includes the type of the document (File or
     * Folder) and its name.
     *
     * @return A string representation of the document.
     */
    @Override
    public String toString() {
        return name;
    }

    public String getCreationDateFormatted() {
        Instant instant = Instant.ofEpochMilli(this.creationDate);
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
        this.creationDateFormatted = getCreationDateFormatted();
    }
}
