package io.github.thegatesdev.maple.exception;

/**
 * This exception is raised when invalid JSON data is written or read.
 */
public final class InvalidJsonException extends RuntimeException {
    // TODO rename this, only used for invalid json output...

    private static final String MESSAGE = "Invalid JSON";


    /**
     * Create the exception with the given message.
     *
     * @param message the detail message
     */
    public InvalidJsonException(String message) {
        super(message);
    }

    /**
     * Create the exception with the given message and cause.
     *
     * @param message the detail message
     * @param cause   the cause
     */
    public InvalidJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create the exception with the given cause.
     *
     * @param cause the cause
     */
    public InvalidJsonException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
