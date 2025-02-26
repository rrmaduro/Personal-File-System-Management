package pt.pa;

/**
 * The Folder class represents a specific type of document in a file system,
 * which is a folder containing other documents (files or sub-folders).
 * It extends the Document class and adds functionality related to managing
 * documents within the folder.
 *
 * @author [Author's Name]
 * @version 1.0
 */
public class Folder extends Document implements Copyable {

    private boolean isAccessable;
    private long sizeInBytes;
    private static final long DEFAULT_SIZE_BYTES = 3000;

    /**
     * Constructs a Folder with the specified name.
     *
     * @param name The name of the folder.
     */
    public Folder(String name) {
        super(name);
        isAccessable = true;
        this.sizeInBytes = DEFAULT_SIZE_BYTES;
    }

    /**
     * Constructs a Folder with the specified document.
     *
     * @param document The document representing the folder.
     */
    public Folder(Document document) {
        super(document.getName());
        isAccessable = true;
    }

    /**
     * Checks if the folder is accessible.
     *
     * @return True if the folder is accessible, false otherwise.
     */
    public boolean isAccessable() {
        return isAccessable;
    }

    /**
     * Sets the accessibility status of the folder.
     *
     * @param accessable True to make the folder accessible, false otherwise.
     */
    public void setAccess(boolean accessable) {
        isAccessable = accessable;
    }


    /**
     * Retrieves the size of the folder in bytes.
     *
     * @return The size of the folder in bytes.
     */
    public long getSizeInBytes() {
        return sizeInBytes;
    }

    /**
     * Provides a string representation of the folder.
     * The string representation includes the name of the folder.
     *
     * @return A string representation of the folder.
     */
    @Override
    public String toString() {
        return " " + getName();
    }

    /**
     * Renames the folder with a new name.
     * <p>
     * This method allows changing the name of the folder to the specified
     * new name. Additionally, it renames all documents within the folder to
     * match the new name.
     *
     * @param name The new name for the folder.
     */
    @Override
    public void rename(String name) {
        super.rename(name);
    }

    /**
     * Creates a copy of the folder.
     * <p>
     * This method creates a new folder with a name derived from the original
     * folder's name and appends "_copy".
     *
     * @return A copy of the folder.
     */
    @Override
    public Folder copy() {
        Folder copy = new Folder(this.getName() + "_copy");
        return copy;
    }
}