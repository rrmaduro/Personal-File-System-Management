/**
 * The InvalidNameException class represents an exception that is thrown when
 * an operation cannot be completed due to an invalid or unsupported name.
 * This exception is typically thrown to indicate that an operation, such as
 * creating, renaming, or searching for a document, could not be completed due
 * to the specified name being invalid or unsupported.
 */
package pt.pa.Exceptions;

public class InvalidNameException extends RuntimeException {

    /**
     * Constructs an InvalidNameException with the specified detail message.
     *
     * @param message The detail message describing the cause of the exception.
     */
    public InvalidNameException(String message) {
        super(message);
    }
}
