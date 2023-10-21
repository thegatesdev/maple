package io.github.thegatesdev.maple.exception;

import io.github.thegatesdev.maple.ElementType;

public class ExpectedTypeError extends ElementTypeException {

    private static final String MESSAGE = "This element should be %s, but %s was found";

    public ExpectedTypeError(ElementType expectedType, ElementType actualType) {
        super(MESSAGE.formatted(expectedType.getInlineName(), expectedType.getInlineName()),
                expectedType,
                actualType);
    }
}
