package io.github.thegatesdev.maple.exception;

import io.github.thegatesdev.maple.ElementType;

public class ElementTypeException extends IllegalArgumentException {

    private static final String MESSAGE = "Invalid element type, expected %s, got %s";
    private final ElementType expectedType, actualType;

    public ElementTypeException(ElementType expectedType, ElementType actualType) {
        super(MESSAGE.formatted(expectedType.getInlineName(), actualType.getInlineName()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public ElementType getExpectedType() {
        return expectedType;
    }

    public ElementType getActualType() {
        return actualType;
    }
}
