package com.github.thegatesdev.maple.exception;

import com.github.thegatesdev.maple.element.ElementType;

/**
 * This exception is raised when a method expects an element to be of some type, but it is of some other type.
 *
 * @author Timar Karels
 */
public final class ElementTypeException extends ElementException {

    private static final String MESSAGE = "Invalid element type; expected %s, got %s";
    private final ElementType expectedType, actualType;

    /**
     * Create the exception with the expected and actual type of the element.
     *
     * @param expectedType the expected type
     * @param actualType   the actual type
     */
    public ElementTypeException(ElementType expectedType, ElementType actualType) {
        super(MESSAGE.formatted(expectedType.name(), actualType.name()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    /**
     * Get the type that was expected from the element.
     *
     * @return the expected type
     */
    public ElementType expectedType() {
        return expectedType;
    }

    /**
     * Get the actual type of the element.
     *
     * @return the actual type
     */
    public ElementType actualType() {
        return actualType;
    }
}
