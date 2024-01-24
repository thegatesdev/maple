package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.*;
import com.github.thegatesdev.maple.exception.ElementTypeException;

import java.util.Optional;

public sealed interface Element permits DictElement, ListElement, BoolElement, DoubleElement, FloatElement, IntElement, LongElement, StringElement, UnsetElement {

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


    default Optional<DictElement> grabDict() {
        return Optional.empty();
    }

    default Optional<ListElement> grabList() {
        return Optional.empty();
    }

    default Optional<String> grabString() {
        return Optional.empty();
    }

    default Optional<Boolean> grabBool() {
        return Optional.empty();
    }

    default Optional<Number> grabNumber() {
        return Optional.empty();
    }
}
