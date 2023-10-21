package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.ElementType;
import io.github.thegatesdev.maple.exception.ExpectedTypeError;
import io.github.thegatesdev.maple.exception.InvalidTypeException;

import java.util.function.Consumer;

public sealed interface DataElement permits DataList, DataMap, DataValue {

    // Self

    DataElement structureCopy();

    // Value

    boolean isEmpty();

    default boolean isPresent() {
        return !isEmpty();
    }

    Object getValue();

    // Type

    default boolean isMap() {
        return false;
    }

    default boolean isList() {
        return false;
    }

    default boolean isValue() {
        return false;
    }


    ElementType getType();


    default DataMap asMap() throws InvalidTypeException {
        throw new InvalidTypeException(ElementType.MAP, getType());
    }

    default DataList asList() throws InvalidTypeException {
        throw new InvalidTypeException(ElementType.LIST, getType());
    }

    default DataValue<?> asValue() throws InvalidTypeException {
        throw new InvalidTypeException(ElementType.VALUE, getType());
    }


    default DataMap requireMap() throws ExpectedTypeError {
        throw new ExpectedTypeError(ElementType.MAP, getType());
    }

    default DataList requireList() throws ExpectedTypeError {
        throw new ExpectedTypeError(ElementType.LIST, getType());
    }

    default DataValue<?> requireValue() throws ExpectedTypeError {
        throw new ExpectedTypeError(ElementType.VALUE, getType());
    }


    default void ifMap(Consumer<DataMap> action, Runnable elseAction) {
        if (isMap()) action.accept(asMap());
        else if (elseAction != null) elseAction.run();
    }

    default void ifMap(Consumer<DataMap> action) {
        ifMap(action, null);
    }

    default void ifList(Consumer<DataList> action, Runnable elseAction) {
        if (isList()) action.accept(asList());
        else if (elseAction != null) elseAction.run();
    }

    default void ifList(Consumer<DataList> action) {
        ifList(action, null);
    }

    default void ifValue(Consumer<DataValue<?>> action, Runnable elseAction) {
        if (isValue()) action.accept(asValue());
        else if (elseAction != null) elseAction.run();
    }

    default void ifValue(Consumer<DataValue<?>> action) {
        ifValue(action, null);
    }
}
