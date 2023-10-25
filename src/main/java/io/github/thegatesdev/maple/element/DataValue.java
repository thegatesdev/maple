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

public sealed interface DataValue<Type> extends DataElement permits DynamicDataValue, StaticDataValue {

    // Self

    @Override
    default DataValue<Type> structureCopy() {
        return this;
    }

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
        if (!isValueOf(otherType)) throw new ValueTypeException(otherType, getValueType());
        return getValueUnsafe();
    }

    default <T> T getValueOrNull(Class<T> otherType) {
        return isValueOf(otherType) ? getValueUnsafe() : null;
    }

    default <T> T getValueOr(Class<T> otherType, T def) {
        var value = getValueOrNull(otherType);
        return value != null ? value : def;
    }

    @SuppressWarnings("unchecked")
    default <T> DataValue<T> getAsHolding(Class<T> otherType) {
        if (!isValueOf(otherType)) throw new ValueTypeException(otherType, getValueType());
        return ((DataValue<T>) this);
    }


    <O> DataValue<O> transform(Class<O> newType, Function<Type, O> function);

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
