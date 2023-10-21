package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.ElementType;
import io.github.thegatesdev.maple.exception.ElementMismatchException;

import java.util.function.Consumer;

public sealed interface DataElement permits DataList, DataMap, DataValue {

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


    default DataMap asMap() throws ElementMismatchException {
        throw new ElementMismatchException(ElementType.MAP, getType());
    }

    default DataList asList() throws ElementMismatchException {
        throw new ElementMismatchException(ElementType.LIST, getType());
    }

    default DataValue<?> asValue() throws ElementMismatchException {
        throw new ElementMismatchException(ElementType.VALUE, getType());
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
