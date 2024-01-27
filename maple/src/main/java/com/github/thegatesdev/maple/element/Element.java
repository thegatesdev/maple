package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.*;
import com.github.thegatesdev.maple.exception.ElementTypeException;

public sealed interface Element permits DictElement, ListElement, BoolElement, DoubleElement, FloatElement, IntElement, LongElement, StringElement, UnsetElement {

    static Element of(String value) {
        return StringElement.of(value);
    }

    static Element of(boolean value) {
        return BoolElement.of(value);
    }

    static Element of(int value) {
        return DoubleElement.of(value);
    }

    static Element of(long value) {
        return LongElement.of(value);
    }

    static Element of(float value) {
        return FloatElement.of(value);
    }

    static Element of(double value) {
        return DoubleElement.of(value);
    }

    static Element ofUnset() {
        return UnsetElement.getInstance();
    }


    ElementType type();


    default boolean isDict() {
        return false;
    }

    default boolean isList() {
        return false;
    }

    default boolean isString() {
        return false;
    }

    default boolean isBool() {
        return false;
    }

    default boolean isNumber() {
        return false;
    }

    default boolean isUnset() {
        return false;
    }


    default DictElement getDict() {
        throw new ElementTypeException(ElementType.DICTIONARY, type());
    }

    default ListElement getList() {
        throw new ElementTypeException(ElementType.LIST, type());
    }

    default String getString() {
        throw new ElementTypeException(ElementType.STRING, type());
    }

    default boolean getBool() {
        throw new ElementTypeException(ElementType.BOOLEAN, type());
    }

    default int getInt() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    default long getLong() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    default float getFloat() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    default double getDouble() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }
}
