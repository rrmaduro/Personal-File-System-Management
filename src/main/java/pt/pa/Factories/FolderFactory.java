package pt.pa.Factories;

import pt.pa.Folder;

/**
 * The FolderFactory class implements the DocumentFactory interface and is responsible for creating Folder instances.
 * It provides methods to create new Folder instances and create a copy of an existing Folder.
 */
public class FolderFactory implements DocumentFactory {

    /**
     * Creates a new Folder with the specified name.
     *
     * @param name The name of the new Folder.
     * @return A new Folder instance with the given name.
     */
    @Override
    public Folder create(String name) {
        return new Folder(name);
    }

    /**
     * Creates a copy of the provided Folder.
     *
     * @param original The original Folder to be copied.
     * @return A new Folder instance that is a copy of the original Folder.
     */
    public Folder createCopy(Folder original) {
        return new Folder(original);
    }
}
