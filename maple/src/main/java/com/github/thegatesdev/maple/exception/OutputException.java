package com.github.thegatesdev.maple.exception;

public final class OutputException extends Exception {

    public OutputException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutputException(Throwable cause) {
        super(cause);
    }
}
