/**
 * The DocumentNotFoundException class represents an exception that is thrown when
 * a document is not found in a file system or a collection.
 * This exception is typically thrown to indicate that an operation, such as searching
 * or accessing a document, could not be completed because the specified document was not found.
 */
package pt.pa.Exceptions;

public class DocumentNotFoundException extends RuntimeException {

    /**
     * Constructs a DocumentNotFoundException with the specified detail message.
     *
     * @param message The detail message describing the cause of the exception.
     */
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
