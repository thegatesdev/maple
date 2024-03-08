package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.*;
import com.github.thegatesdev.maple.exception.ElementTypeException;

/**
 * A single element in a structure.
 *
 * @author Timar Karels
 */
public sealed interface Element permits DictElement, ElementCollection, ListElement, BoolElement, DoubleElement, FloatElement, IntElement, LongElement, StringElement, UnsetElement {

    /**
     * Get an element representing the given string value.
     *
     * @param value the value to represent
     * @return the element representing the value
     * @throws NullPointerException if the given string is null
     */
    static Element of(String value) {
        return new StringElement(value);
    }

    /**
     * Get an element representing the given boolean value.
     *
     * @param value the value to represent
     * @return the element representing the value
     */
    static Element of(boolean value) {
        return BoolElement.of(value);
    }

    /**
     * Get an element representing the given int value.
     *
     * @param value the value to represent
     * @return the element representing the value
     */
    static Element of(int value) {
        return DoubleElement.of(value);
    }

    /**
     * Get an element representing the given long value.
     *
     * @param value the value to represent
     * @return the element representing the value
     */
    static Element of(long value) {
        return LongElement.of(value);
    }

    /**
     * Get an element representing the given float value.
     *
     * @param value the value to represent
     * @return the element representing the value
     */
    static Element of(float value) {
        return FloatElement.of(value);
    }

    /**
     * Get an element representing the given double value.
     *
     * @param value the value to represent
     * @return the element representing the value
     */
    static Element of(double value) {
        return DoubleElement.of(value);
    }

    /**
     * Get an element representing no value.
     * This is the equivalent of a 'null' value in the JSON specification.
     *
     * @return the element representing no value
     */
    static Element unset() {
        return UnsetElement.INSTANCE;
    }


    /**
     * Get the element type of this element.
     *
     * @return the type of this element
     */
    ElementType type();


    /**
     * Check whether this element is a dictionary element.
     *
     * @return true if this element is a dictionary element
     */
    default boolean isDict() {
        return false;
    }

    /**
     * Check whether this element is a list element.
     *
     * @return true if this element is a list element
     */
    default boolean isList() {
        return false;
    }

    /**
     * Check whether this element represents a string value.
     *
     * @return true if this element represents a string value
     */
    default boolean isString() {
        return false;
    }

    /**
     * Check whether this element represents a boolean value.
     *
     * @return true if this element represents a boolean value
     */
    default boolean isBool() {
        return false;
    }

    /**
     * Check whether this element represents a number value.
     *
     * @return true if this element represents a number value
     */
    default boolean isNumber() {
        return false;
    }

    /**
     * Check whether this element represents no value.
     * This is the equivalent of a 'null' value in the JSON specification.
     *
     * @return true if this element represents no value
     */
    default boolean isUnset() {
        return false;
    }


    /**
     * Get this element as a dictionary element, if applicable.
     *
     * @return this element
     * @throws ElementTypeException if this element is not a dictionary element
     */
    default DictElement getDict() {
        throw new ElementTypeException(ElementType.DICTIONARY, type());
    }

    /**
     * Get this element as a list element, if applicable.
     *
     * @return this element
     * @throws ElementTypeException if this element is not a list element
     */
    default ListElement getList() {
        throw new ElementTypeException(ElementType.LIST, type());
    }

    /**
     * Get the string value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a string value
     */
    default String getString() {
        throw new ElementTypeException(ElementType.STRING, type());
    }

    /**
     * Get the boolean value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a boolean value
     */
    default boolean getBool() {
        throw new ElementTypeException(ElementType.BOOLEAN, type());
    }

    /**
     * Get the short value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default short getShort() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    /**
     * Get the int value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default int getInt() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    /**
     * Get the long value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default long getLong() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    /**
     * Get the float value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default float getFloat() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    /**
     * Get the double value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default double getDouble() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }
}
