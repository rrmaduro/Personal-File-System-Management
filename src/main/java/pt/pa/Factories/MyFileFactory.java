package pt.pa.Factories;

import pt.pa.MyFile;

/**
 * The MyFileFactory class implements the DocumentFactory interface and is responsible for creating MyFile instances.
 * It provides methods to create new MyFile instances and create a copy of an existing MyFile.
 */
public class MyFileFactory implements DocumentFactory {

    /**
     * Creates a new MyFile with the specified name and extension.
     *
     * @param name      The name of the new MyFile.
     * @param extension The file extension for the new MyFile.
     * @return A new MyFile instance with the given name and extension.
     */
    public MyFile create(String name, String extension) {
        return new MyFile(name, extension);
    }

    /**
     * Creates a new MyFile with the specified name and a default ".txt" extension.
     *
     * @param name The name of the new MyFile.
     * @return A new MyFile instance with the given name and default extension.
     */
    @Override
    public MyFile create(String name) {
        return new MyFile(name, ".txt");
    }

    /**
     * Creates a copy of the provided MyFile.
     *
     * @param original The original MyFile to be copied.
     * @return A new MyFile instance that is a copy of the original MyFile.
     */
    public MyFile createCopy(MyFile original) {
        return (MyFile) original.copy();
    }
}
