package pt.pa.Exceptions;

/**
 * The NoMementoException class extends RuntimeException and is thrown when an operation requiring a Memento
 * cannot proceed because there is no Memento available.
 */
public class NoMementoException extends RuntimeException {

    /**
     * Constructs a NoMementoException with a default error message.
     * The default message is "There is no Memento".
     */
    public NoMementoException() {
        super("There is no Memento");
    }

    /**
     * Constructs a NoMementoException with a specified error message.
     *
     * @param s The detail message providing more information about the exception.
     */
    public NoMementoException(String s) {
        super(s);
    }
}
