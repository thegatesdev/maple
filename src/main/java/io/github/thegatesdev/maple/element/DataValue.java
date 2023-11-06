/*
Copyright 2023 Timar Karels

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

package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.ElementType;
import io.github.thegatesdev.maple.exception.ValueTypeException;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An element holding a single value type that may change.
 */
public sealed interface DataValue<Type> extends DataElement permits DynamicValue, StaticValue {

    /**
     * Create a new static value element containing the given value.
     *
     * @param value the value to wrap
     * @return the new value element
     */
    @SuppressWarnings("unchecked")
    static <Type> DataValue<Type> of(Type value) {
        return new StaticValue<>(((Class<Type>) value.getClass()), value);
    }

    /**
     * Create a new dynamic value element producing values of the given type.
     *
     * @param valueType     the type of values produced
     * @param valueSupplier the supplier producing the values
     * @return the new value element
     */
    static <Type> DataValue<Type> of(Class<Type> valueType, Supplier<Type> valueSupplier) {
        return new DynamicValue<>(valueType, valueSupplier);
    }

    // Operations

    @Override
    default DataElement crawl(Function<DataElement, DataElement> crawlFunction) {
        return this; // A value does not have descendants
    }

    @Override
    default DataValue<Type> transform(Function<DataElement, DataElement> transformFunction) {
        return this;
    }

    // Value type

    /**
     * @return the type of the contained value
     */
    Class<Type> valueType();

    /**
     * @param otherType the type to check for
     * @return {@code true} if this element contains a value that can be assigned to the given type
     */
    default boolean isValueOf(Class<?> otherType) {
        return otherType.isAssignableFrom(valueType());
    }

    /**
     * @return the contained value cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    default <T> T valueUnsafe() {
        return (T) value();
    }

    /**
     * @param otherType the type to get the value as
     * @return the contained value as {@code T}
     * @throws ValueTypeException if the types do not match
     */
    default <T> T valueOrThrow(Class<T> otherType) {
        if (!isValueOf(otherType)) throw new ValueTypeException(otherType, valueType());
        return valueUnsafe();
    }

    /**
     * @param otherType the type to get the value as
     * @return the contained value as {@code T}, or {@code null} if the type does not match
     */
    default <T> T valueOrNull(Class<T> otherType) {
        return isValueOf(otherType) ? valueUnsafe() : null;
    }

    /**
     * @param otherType the type to get the value as
     * @param def       the default value
     * @return the contained value as {@code T}, or the given default value if the type does not match
     */
    default <T> T valueOr(Class<T> otherType, T def) {
        var value = valueOrNull(otherType);
        return value != null ? value : def;
    }

    /**
     * @param otherType the required type of this element
     * @return this value element as holding a value of type {@code T}
     * @throws ValueTypeException if the types do not match
     */
    @SuppressWarnings("unchecked")
    default <T> DataValue<T> asHolding(Class<T> otherType) {
        if (!isValueOf(otherType)) throw new ValueTypeException(otherType, valueType());
        return ((DataValue<T>) this);
    }


    /**
     * Transform this value element with the given function.
     * <ul>
     * <li> If this element is static, a static element is returned holding the result from applying the transformation function.
     * <li> If this element is dynamic, a dynamic element is returned, that will apply the transformation function each time it is retrieved.
     * </ul>
     * This function is used to preserve the dynamic nature of elements.
     *
     * @param newType  the value type of the new value element
     * @param function the transformation function
     * @return a new value element holding the given value type
     */
    <O> DataValue<O> transform(Class<O> newType, Function<Type, O> function);

    // Self type


    @Override
    default ElementType type() {
        return ElementType.VALUE;
    }

    @Override
    default boolean isValue() {
        return true;
    }

    /**
     * Retrieve a value from this element.
     * May be different each call.
     *
     * @return a value of type {@code Type}
     */
    Type value();

    @Override
    default DataValue<Type> asValue() {
        return this;
    }

    @Override
    default boolean isEmpty() {
        return false;
    }
}
