package com.github.thegatesdev.maple.exception;

/**
 * This exception is raised when the input for an adapter is invalid for the specification.
 */
public final class AdaptException extends Exception {
    /**
     * Create the exception with the given message.
     *
     * @param msg the message describing the cause of the exception
     */
    public AdaptException(String msg) {
        super(msg);
    }

    /**
     * Create the exception with the given message and cause.
     *
     * @param msg   the message describing where the problem happened
     * @param cause the throwable that caused this exception to be raised
     */
    public AdaptException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create the exception with the given cause.
     * Consider using {@link #AdaptException(String, Throwable)} instead.
     *
     * @param cause the throwable that caused this exception to be raised
     */
    public AdaptException(Throwable cause) {
        super(cause);
    }
}
