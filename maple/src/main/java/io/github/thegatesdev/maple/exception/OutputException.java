package io.github.thegatesdev.maple.exception;

/**
 * This exception is a runtime wrapper for any raised IO exceptions while outputting data.
 */
public final class OutputException extends RuntimeException {

    private static final String MESSAGE = "Output failed: IO error";


    /**
     * Create the exception with the given cause.
     *
     * @param cause the wrapped exception
     */
    public OutputException(Throwable cause) {
        super(MESSAGE, cause);
    }

    /**
     * Create the exception with the given message.
     *
     * @param message the message
     */
    public OutputException(String message) {
        super(message);
    }
}
