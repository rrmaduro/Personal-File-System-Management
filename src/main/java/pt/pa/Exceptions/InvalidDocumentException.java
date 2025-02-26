/**
 * The InvalidDocumentException class represents an exception that is thrown when
 * an operation on a document cannot be completed due to the document being invalid
 * or having an unsupported type.
 * This exception is typically thrown to indicate that an operation, such as copying
 * or moving a document, could not be completed because the specified document has an
 * unsupported or invalid type.
 */
package pt.pa.Exceptions;

public class InvalidDocumentException extends RuntimeException {

    /**
     * Constructs an InvalidDocumentException with the specified detail message.
     *
     * @param message The detail message describing the cause of the exception.
     */
    public InvalidDocumentException(String message) {
        super(message);
    }
}
