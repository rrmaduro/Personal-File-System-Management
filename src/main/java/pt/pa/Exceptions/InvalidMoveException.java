/**
 * The InvalidMoveException class represents an exception that is thrown when
 * a move operation on a document or position cannot be completed due to invalid
 * or unsupported conditions.
 * This exception is typically thrown to indicate that a move operation, such as
 * moving a document or position within a file system, could not be completed due
 * to the specified move being invalid or unsupported.
 */
package pt.pa.Exceptions;

public class InvalidMoveException extends RuntimeException {

    /**
     * Constructs an InvalidMoveException with the specified detail message.
     *
     * @param message The detail message describing the cause of the exception.
     */
    public InvalidMoveException(String message) {
        super(message);
    }
}
