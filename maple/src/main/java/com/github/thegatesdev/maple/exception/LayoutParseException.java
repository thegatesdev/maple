package com.github.thegatesdev.maple.exception;

/**
 * This exception is raised when a layout fails to parse an element.
 */
public class LayoutParseException extends Exception {

    /**
     * Create the exception with the given message.
     *
     * @param msg the message describing the cause of the exception
     */
    public LayoutParseException(String msg) {
        super(msg);
    }

    /**
     * Create the exception with the given message and cause.
     *
     * @param msg   the message describing where the problem happened
     * @param cause the throwable that caused this exception to be raised
     */
    public LayoutParseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create the exception with the given cause.
     * Consider using {@link #LayoutParseException(String, Throwable)} instead.
     *
     * @param cause the throwable that caused this exception to be raised
     */
    public LayoutParseException(Throwable cause) {
        super(cause);
    }
}
