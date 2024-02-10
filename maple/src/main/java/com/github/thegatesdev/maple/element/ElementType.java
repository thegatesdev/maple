package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.exception.ElementTypeException;

/**
 * The different types of elements that are available.
 * These all represent some value from the JSON specification.
 */
public enum ElementType {
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

    /**
     * Return the inputted element if its type matches with this element type.
     *
     * @param element the element to match to
     * @return the same element
     * @throws ElementTypeException if the type does not match
     */
    public Element match(Element element) {
        if (element.type() != this) throw new ElementTypeException(this, element.type());
        return element;
    }
}
