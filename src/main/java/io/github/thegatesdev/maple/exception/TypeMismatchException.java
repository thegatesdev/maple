package io.github.thegatesdev.maple.exception;

public class TypeMismatchException extends IllegalArgumentException {

    private final Class<?> expectedType, actualType;

    public TypeMismatchException(Class<?> expectedType, Class<?> actualType) {
        super("Expected '%s', got '%s'".formatted(expectedType.getSimpleName(), actualType.getSimpleName()));
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
