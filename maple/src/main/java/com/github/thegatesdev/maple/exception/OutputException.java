package com.github.thegatesdev.maple.exception;

import java.util.*;

public final class OutputException extends RuntimeException {

    private static final String MESSAGE = "Output failed: IO error";

    private final String context;

    public OutputException(Throwable cause, String context) {
        super(MESSAGE, cause);
        this.context = context;
    }

    public OutputException(Throwable cause) {
        this(cause, null);
    }


    public Optional<String> context() {
        return Optional.ofNullable(context);
    }
}
