/**
 * The InvalidFileException class represents an exception that is thrown when
 * an operation on a file cannot be completed due to the file being invalid.
 * This exception is typically thrown to indicate that an operation, such as
 * reading or processing a file, could not be completed because the specified file
 * is invalid or has an unsupported format.
 */
package pt.pa.Exceptions;

public class InvalidFileException extends RuntimeException {

    /**
     * Constructs an InvalidFileException with the specified detail message.
     *
     * @param message The detail message describing the cause of the exception.
     */
    public InvalidFileException(String message) {
        super(message);
    }
}
