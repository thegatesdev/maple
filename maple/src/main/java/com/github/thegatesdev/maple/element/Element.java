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

    default DictElement getDict(DictElement def) {
        return def;
    }

    default ListElement getList() {
        throw new ElementTypeException(ElementType.LIST, type());
    }

    default ListElement getDict(ListElement def) {
        return def;
    }

    default String getString() {
        throw new ElementTypeException(ElementType.STRING, type());
    }

    default String getString(String def) {
        return def;
    }

    default boolean getBool() {
        throw new ElementTypeException(ElementType.BOOLEAN, type());
    }

    default boolean getBool(boolean def) {
        return def;
    }

    default int getInt() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    default int getInt(int def) {
        return def;
    }

    default long getLong() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    default long getLong(long def) {
        return def;
    }

    default float getFloat() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    default float getFloat(float def) {
        return def;
    }

    default double getDouble() {
        throw new ElementTypeException(ElementType.NUMBER, type());
    }

    default double getDouble(double def) {
        return def;
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
