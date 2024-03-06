package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.*;
import com.github.thegatesdev.maple.exception.ElementTypeException;

import java.util.Collection;
import java.util.Map;

/**
 * A single element in a structure.
 */
public sealed interface Element permits DictElement, ElementCollection, ListElement, BoolElement, DoubleElement, FloatElement, IntElement, LongElement, ShortElement, StringElement, UnsetElement {

    /**
     * Get a list element containing the values from the given array.
     *
     * @param values the values for the list
     * @return the list element containing the values
     * @throws NullPointerException if the given array is null
     */
    static ListElement of(Element[] values) {
        return MemoryListElement.of(values);
    }

    /**
     * Get a list element containing the values from the given collection.
     *
     * @param values the values for the list
     * @return the list element containing the values
     * @throws NullPointerException if the given collection is null
     */
    static ListElement of(Collection<Element> values) {
        return MemoryListElement.of(values);
    }

    /**
     * Get a dictionary element containing the keys and values from the given map.
     *
     * @param values the values for the dictionary
     * @return the dictionary element containing the values
     * @throws NullPointerException if the given map is null
     */
    static DictElement of(Map<String, Element> values) {
        return MemoryDictElement.of(values);
    }

    /**
     * Get an element containing the given string value.
     *
     * @param value the value to contain
     * @return the element containing the value
     * @throws NullPointerException if the given string is null
     */
    static Element of(String value) {
        return StringElement.of(value);
    }

    /**
     * Get an element containing the given boolean value.
     *
     * @param value the value to contain
     * @return the element containing the value
     */
    static Element of(boolean value) {
        return BoolElement.of(value);
    }

    /**
     * Get an element containing the given short value.
     *
     * @param value the value to contain
     * @return the element containing the value
     */
    static Element of(short value) {
        return ShortElement.of(value);
    }

    /**
     * Get an element containing the given int value.
     *
     * @param value the value to contain
     * @return the element containing the value
     */
    static Element of(int value) {
        return DoubleElement.of(value);
    }

    /**
     * Get an element containing the given long value.
     *
     * @param value the value to contain
     * @return the element containing the value
     */
    static Element of(long value) {
        return LongElement.of(value);
    }

    /**
     * Get an element containing the given float value.
     *
     * @param value the value to contain
     * @return the element containing the value
     */
    static Element of(float value) {
        return FloatElement.of(value);
    }

    /**
     * Get an element containing the given double value.
     *
     * @param value the value to contain
     * @return the element containing the value
     */
    static Element of(double value) {
        return DoubleElement.of(value);
    }

    /**
     * Get an element containing no value.
     * This is the equivalent of a 'null' value in the JSON specification.
     *
     * @return the element containing no value
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
     * Check whether this element contains a string value.
     *
     * @return true if this element contains a string value
     */
    default boolean isString() {
        return false;
    }

    /**
     * Check whether this element contains a boolean value.
     *
     * @return true if this element contains a boolean value
     */
    default boolean isBool() {
        return false;
    }

    /**
     * Check whether this element contains a number value.
     *
     * @return true if this element contains a number value
     */
    default boolean isNumber() {
        return false;
    }

    /**
     * Check whether this element contains no value.
     * This is the equivalent of a 'null' value in the JSON specification.
     *
     * @return true if this element contains no value
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
     * Get the string value contained in this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not contain a string value
     */
    default String getString() {
        throw new ElementTypeException(ElementType.STRING, type());
    }

    /**
     * Get the boolean value contained in this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not contain a boolean value
     */
    default boolean getBool() {
        throw new ElementTypeException(ElementType.BOOLEAN, type());
    }

    /**
     * Get the short value contained in this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not contain a number value
     */
    default short getShort() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    /**
     * Get the int value contained in this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not contain a number value
     */
    default int getInt() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    /**
     * Get the long value contained in this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not contain a number value
     */
    default long getLong() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    /**
     * Get the float value contained in this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not contain a number value
     */
    default float getFloat() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    /**
     * Get the double value contained in this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not contain a number value
     */
    default double getDouble() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }
}
