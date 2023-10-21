package io.github.thegatesdev.maple.exception;

import io.github.thegatesdev.maple.ElementType;

public class ElementMismatchException extends IllegalArgumentException {

    private final ElementType expectedType, actualType;

    public ElementMismatchException(ElementType expectedType, ElementType actualType) {
        super("Invalid element type, expected %s, got %s".formatted(expectedType.getInlineName(), actualType.getInlineName()));
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
