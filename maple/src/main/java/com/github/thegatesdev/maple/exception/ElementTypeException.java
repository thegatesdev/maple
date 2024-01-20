package com.github.thegatesdev.maple.exception;

import com.github.thegatesdev.maple.element.ElementType;

public final class ElementTypeException extends ElementException {

    private static final String MESSAGE = "Invalid element type; expected %s, got %s";
    private final ElementType expectedType, actualType;

    public ElementTypeException(ElementType expectedType, ElementType actualType) {
        super(MESSAGE.formatted(expectedType.name(), actualType.name()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public ElementType expectedType() {
        return expectedType;
    }

    public ElementType actualType() {
        return actualType;
    }
}
