package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.element.impl.*;
import io.github.thegatesdev.maple.element.impl.internal.*;
import io.github.thegatesdev.maple.exception.*;
import io.github.thegatesdev.maple.io.*;

import java.math.*;

/**
 * A single element in a structure.
 *
 * <p>
 * All elements are immutable, and any operations are guaranteed to be thread safe.
 * This doesn't include the element builders.
 * <p>
 * It is recommended to interact with the methods defined in this interface,
 * rather than using the specific primitive implementations like {@code IntElement} and {@code StringElement}.
 * Those are exposed for complex tasks where the actual type matters, like serialization.
 * <p>
 * Elements that represent numbers will always cast to the number type that is asked for, regardless of data loss.
 * Take great care to enforce the largest possible value on user input, before trying to obtain it.
 *
 * @author Timar Karels
 */
public sealed interface Element extends Source permits DictElement,
    ElementCollection,
    ListElement,
    BigDecimalElement,
    BigIntegerElement,
    BoolElement,
    DoubleElement,
    FloatElement,
    IntElement,
    LongElement,
    StringElement,
    NullElement,
    NumberElement {

    /**
     * Get an element representing the given string value.
     *
     * @param value the value to represent
     * @return the element representing the value
     * @throws NullPointerException if the given string is null
     */
    static Element of(String value) {
        return StringElement.of(value);
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
        return IntElement.of(value);
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
     * Get an element representing the given big integer value.
     *
     * @param value the value to represent
     * @return the element representing the value
     */
    static Element of(BigInteger value) {
        return BigIntegerElement.of(value);
    }

    /**
     * Get an element representing the given big decimal value.
     *
     * @param value the value to represent
     * @return the element representing the value
     */
    static Element of(BigDecimal value) {
        return BigDecimalElement.of(value);
    }

    /**
     * Get an element representing no value.
     * This is the equivalent of a 'null' value in the JSON specification.
     *
     * @return the element representing no value
     */
    static Element none() {
        return NullElement.INSTANCE;
    }


    /**
     * Get the element type of this element.
     *
     * @return the type of this element
     */
    ElementType type();

    /**
     * Get the string representation of this element.
     * This always includes the type of the element, and additional information.
     * For elements with a single value, this will be the value in string form.
     * For dictionary and list elements, this will be the size of the collection.
     * <p>
     * Examples:
     * <ul>
     *     <li>{@code Element.of(5)} will produce "{@code number<5I>}"</li>
     *     <li>{@code Element.of("foo")} will produce "{@code string<foo>}"</li>
     *     <li>{@code DictElement.empty()} will produce "{@code dict{0}}"</li>
     * </ul>
     *
     * @return the string representation of this element
     */
    String toString();

    /**
     * Get the value of this element as a string.
     * For elements containing multiple values, this method acts the same as {@link #toString()}.
     *
     * @return the value of this element as a string
     */
    String stringValue();


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
    default boolean isNull() {
        return false;
    }


    /**
     * Get this element as a dictionary element, if applicable.
     *
     * @return this element
     * @throws ElementTypeException if this element is not a dictionary element
     */
    default DictElement getDict() {
        throw new ElementTypeException(ElementType.DICT, type());
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
        throw new ElementTypeException(ElementType.BOOL, type());
    }

    /**
     * Get the int value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default int getInt() {
        throw new ElementTypeException(ElementType.NUM, type());
    }

    /**
     * Get the long value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default long getLong() {
        throw new ElementTypeException(ElementType.NUM, type());
    }

    /**
     * Get the float value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default float getFloat() {
        throw new ElementTypeException(ElementType.NUM, type());
    }

    /**
     * Get the double value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default double getDouble() {
        throw new ElementTypeException(ElementType.NUM, type());
    }

    /**
     * Get the big integer value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default BigInteger getBigInteger() {
        throw new ElementTypeException(ElementType.NUM, type());
    }

    /**
     * Get the big decimal value represented by this element, if applicable.
     *
     * @return the value from this element
     * @throws ElementTypeException if this element did not represent a number value
     */
    default BigDecimal getBigDecimal() {
        throw new ElementTypeException(ElementType.NUM, type());
    }
}

/*
Copyright 2024 Timar Karels

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
