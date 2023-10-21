package io.github.thegatesdev.maple.exception;

import io.github.thegatesdev.maple.ElementType;

public class ElementTypeException extends IllegalArgumentException {

    private final ElementType expectedType, actualType;

    public ElementTypeException(String message, ElementType expectedType, ElementType actualType) {
        super(message);
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
