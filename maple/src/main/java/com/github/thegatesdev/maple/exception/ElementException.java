package com.github.thegatesdev.maple.exception;

/**
 * This is a generic exception raised when an issue occurs while operating on an element.
 */
public class ElementException extends RuntimeException {

    /**
     * Create the exception with the given message.
     *
     * @param msg the message describing the cause of the exception
     */
    public ElementException(String msg) {
        super(msg);
    }

    /**
     * Create the exception with the given message and cause.
     *
     * @param msg   the message describing where the problem happened
     * @param cause the throwable that caused this exception to be raised
     */
    public ElementException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create the exception with the given cause.
     * Consider using {@link #ElementException(String, Throwable)} instead.
     *
     * @param cause the throwable that caused this exception to be raised
     */
    public ElementException(Throwable cause) {
        super(cause);
    }
}
