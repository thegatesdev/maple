package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.exception.ElementTypeException;
import com.github.thegatesdev.maple.layout.Layout;

import java.util.Optional;

/**
 * The different types of elements that are available.
 * These all represent some value from the JSON specification.
 */
public enum ElementType implements Layout<Element> {
    /**
     * A dictionary, or mapping, of string keys to element values.
     * Equivalent of a JSON 'object'.
     */
    DICTIONARY,
    /**
     * A sequential list, or array, of element values.
     * Equivalent of a JSON 'array'.
     */
    LIST,
    /**
     * A string value.
     * Equivalent of a JSON 'string'.
     */
    STRING,
    /**
     * A boolean value.
     * Equivalent of the JSON 'true' or 'false' values.
     */
    BOOLEAN,
    /**
     * A number value.
     * Equivalent of the JSON 'number' value.
     * Number elements can store different number representations as in the Java language.
     * Currently present are: int, long, double and float.
     */
    NUMBER,
    /**
     * An unset value.
     * Equivalent of the JSON 'null' value.
     * It is not advised to directly use this element type, it only exists to distinguish between absent and null values.
     */
    UNSET;

    @Override
    public Optional<Element> apply(Element element) {
        if (this != element.type()) throw new ElementTypeException(this, element.type());
        return Optional.empty();
    }
}
