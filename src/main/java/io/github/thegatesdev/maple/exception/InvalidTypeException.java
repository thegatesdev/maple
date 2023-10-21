package io.github.thegatesdev.maple.exception;

import io.github.thegatesdev.maple.ElementType;

public class InvalidTypeException extends ElementTypeException {

    private static final String MESSAGE = "Invalid element type, expected %s, got %s";

    public InvalidTypeException(ElementType expectedType, ElementType actualType) {
        super(MESSAGE.formatted(expectedType.getInlineName(), actualType.getInlineName()),
                expectedType,
                actualType);
    }
}
