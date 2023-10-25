package io.github.thegatesdev.maple.exception;

public class ValueTypeException extends IllegalArgumentException {

    private static final String MESSAGE = "Invalid value type, expected '%s', got '%s'";

    private final Class<?> expectedType, actualType;

    public ValueTypeException(Class<?> expectedType, Class<?> actualType) {
        super(MESSAGE.formatted(expectedType.getSimpleName(), actualType.getSimpleName()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    public Class<?> getActualType() {
        return actualType;
    }
}
