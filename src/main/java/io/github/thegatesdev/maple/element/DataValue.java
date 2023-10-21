package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.ElementType;
import io.github.thegatesdev.maple.exception.TypeMismatchException;

public sealed interface DataValue<Type> extends DataElement permits DynamicDataValue, StaticDataValue {

    // Value type

    Class<Type> getValueType();

    default boolean isValueOf(Class<?> otherType) {
        return otherType.isAssignableFrom(getValueType());
    }

    @SuppressWarnings("unchecked")
    default <T> T getValueUnsafe() {
        return (T) getValue();
    }

    default <T> T getValueOrThrow(Class<T> otherType) {
        if (!isValueOf(otherType)) throw new TypeMismatchException(otherType, getValueType());
        return getValueUnsafe();
    }

    default <T> T getValueOrNull(Class<T> otherType) {
        return isValueOf(otherType) ? getValueUnsafe() : null;
    }

    default <T> T getValueOr(Class<T> otherType, T def) {
        var value = getValueOrNull(otherType);
        return value != null ? value : def;
    }

    // Self type


    @Override
    default ElementType getType() {
        return ElementType.VALUE;
    }

    @Override
    default boolean isValue() {
        return true;
    }

    @Override
    Type getValue();

    @Override
    default DataValue<Type> asValue() {
        return this;
    }
}
