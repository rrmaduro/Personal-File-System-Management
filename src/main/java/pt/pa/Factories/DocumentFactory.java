package pt.pa.Factories;

import pt.pa.Document;

/**
 * The DocumentFactory interface represents a factory for creating documents.
 * Documents can be files or folders in a file system.
 */
public interface DocumentFactory {

    /**
     * Creates a new document with the given name.
     *
     * @param name The name of the new document.
     * @return The newly created document.
     */
    Document create(String name);
}
