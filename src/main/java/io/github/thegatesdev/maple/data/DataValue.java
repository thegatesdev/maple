package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.exception.ElementException;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/*
Copyright (C) 2022  Timar Karels

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

/**
 * An element for holding a single value that may change.
 */
public abstract class DataValue<Value> extends DataElement {
    private static class Static<Value> extends DataValue<Value> {
        private final Value value;
        private final Class<Value> valueType;

        @SuppressWarnings("unchecked")
        protected Static(Value value) {
            this.value = value;
            this.valueType = ((Class<Value>) value.getClass());
        }

        @Override
        public Value value() {
            return value;
        }

        @Override
        public <T> DataValue<T> then(Class<T> newType, Function<Value, T> modify) {
            return new Static<>(modify.apply(value));
        }

        @Override
        public Class<Value> valueType() {
            return valueType;
        }

        @Override
        public DataValue<Value> copy() {
            return new Static<>(value);
        }

        @Override
        public int hashCode() {
            return 31 * value.hashCode();
        }

        @Override
        public String toString() {
            return "value<" + value + ">";
        }
    }

    private static class Dynamic<Value> extends DataValue<Value> {
        private final Supplier<Value> valueSupplier;
        private final Class<Value> valueType;

        protected Dynamic(Class<Value> valueType, Supplier<Value> valueSupplier) {
            this.valueType = valueType;
            this.valueSupplier = valueSupplier;
        }

        @Override
        public Value value() {
            return valueSupplier.get();
        }

        @Override
        public <T> DataValue<T> then(Class<T> newType, Function<Value, T> modify) {
            return new Dynamic<>(newType, () -> modify.apply(valueSupplier.get()));
        }

        @Override
        public Class<Value> valueType() {
            return valueType;
        }

        @Override
        public DataValue<Value> copy() {
            return new Dynamic<>(valueType, valueSupplier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(valueSupplier, valueType);
        }

        @Override
        public String toString() {
            return "value<dynamic " + valueType.getSimpleName() + ">";
        }
    }

    /**
     * Creates a new dataValue holding the supplied value.
     */
    public static <T> DataValue<T> of(T value) {
        return new Static<>(value);
    }

    /**
     * Creates a new dataValue of the supplied valueType, getting the supplied valueSupplier each time the value is accessed.
     */
    public static <T> DataValue<T> of(Class<T> valueType, Supplier<T> valueSupplier) {
        return new Dynamic<>(valueType, valueSupplier);
    }

    // -- UTIL

    /**
     * Creates a new dataValue by applying the supplied modify function.<br>
     * This returns:
     * <li> A new static dataValue holding the modified value if this dataValue is static. </li>
     * <li> A new dynamic dataValue holding the combined functions if this dataValue is dynamic. </li>
     */
    public abstract <T> DataValue<T> then(Class<T> newType, Function<Value, T> modify);

    // -- TYPE

    /**
     * @return The type of the value contained in this value element
     */
    public abstract Class<Value> valueType();

    /**
     * @return {@code true} if this elements type conforms to the supplied expected type
     */
    public boolean valueOf(Class<?> expectedType) {
        return expectedType.isAssignableFrom(valueType());
    }

    /**
     * @return This value element as holding a value of the supplied type
     * @throws ElementException If this element is not of the supplied type
     */
    @SuppressWarnings("unchecked")
    public <T> DataValue<T> requireType(Class<T> clazz) throws ElementException {
        if (!valueOf(clazz)) throw ElementException.requireType(this, clazz);
        return (DataValue<T>) this;
    }

    // -- VALUE

    /**
     * @return The value this value element holds
     */
    public abstract Value value();

    /**
     * @return The value this value element holds
     */
    @Override
    public Value view() {
        return value();
    }

    /**
     * @return The value of this value element, unsafely cast to {@code T}
     */
    @SuppressWarnings("unchecked")
    public <T> T valueUnsafe() {
        return (T) value();
    }

    /**
     * @return The value of this value element as T, or {@code null} if the value is not an instance of the supplied expected type
     */
    public <T> T valueOrNull(Class<T> expectedType) {
        return valueOf(expectedType) ? valueUnsafe() : null;
    }

    /**
     * @return The value of this value element as T, or the supplied public value if the value is not an instance of the supplied expected type
     */
    public <T> T valueOr(Class<T> expectedType, T def) {
        var value = valueOrNull(expectedType);
        return value == null ? def : value;
    }

    /**
     * @return The value of this value element as T
     * @throws ElementException If the value is not an instance of the expected type
     */
    public <T> T valueOrThrow(Class<T> expectedType) throws ElementException {
        requireType(expectedType);
        return valueUnsafe();
    }

    // -- SELF

    @Override
    public abstract DataValue<Value> copy();

    /**
     * @return {@code false}
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /**
     * @return {@code true}
     */
    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public DataValue<Value> asValue() {
        return this;
    }

    @Override
    public String toString() {
        return "value<" + valueType().getSimpleName() + ">";
    }

    public abstract int hashCode();

    // -- GETTERS

    /**
     * @return The boolean value contained in this element
     * @throws ElementException If this element does not contain a boolean value
     */
    public Boolean booleanValue() throws ElementException {
        return valueOrThrow(Boolean.class);
    }

    /**
     * @param def The value to return if this element does not contain a boolean value
     * @return The boolean value contained in this element
     */
    public Boolean booleanValue(boolean def) {
        return valueOr(Boolean.class, def);
    }

    /**
     * @return The integer value contained in this element
     * @throws ElementException If this element does not contain a integer value
     */
    public Integer intValue() throws ElementException {
        return valueOrThrow(Integer.class);
    }

    /**
     * @param def The value to return if this element does not contain an integer value
     * @return The integer value contained in this element
     */
    public Integer intValue(int def) {
        return valueOr(Integer.class, def);
    }

    /**
     * @return The number value contained in this element
     * @throws ElementException If this element does not contain a number value
     */
    public Number numberValue() throws ElementException {
        return valueOrThrow(Number.class);
    }

    /**
     * @param def The value to return if this element does not contain a number value
     * @return The number value contained in this element
     */
    public Number numberValue(int def) {
        return valueOr(Number.class, def);
    }

    /**
     * @return The double value contained in this element
     * @throws ElementException If this element does not contain a number value
     */
    public Double doubleValue() throws ElementException {
        return valueOrThrow(Number.class).doubleValue();
    }

    /**
     * @param def The value to return if this element does not contain a number value
     * @return The double value contained in this element
     */
    public Double doubleValue(double def) {
        return valueOr(Number.class, def).doubleValue();
    }

    /**
     * @return The float value contained in this element
     * @throws ElementException If this element does not contain a number value
     */
    public Float floatValue() throws ElementException {
        return valueOrThrow(Number.class).floatValue();
    }

    /**
     * @param def The value to return if this element does not contain a number value
     * @return The float value contained in this element
     */
    public Float floatValue(double def) {
        return valueOr(Number.class, def).floatValue();
    }

    /**
     * @return The long value contained in this element
     * @throws ElementException If this element does not contain a number value
     */
    public Long longValue() throws ElementException {
        return valueOrThrow(Number.class).longValue();
    }

    /**
     * @param def The value to return if this element does not contain a number value
     * @return The long value contained in this element
     */
    public Long longValue(double def) {
        return valueOr(Number.class, def).longValue();
    }

    /**
     * @return The string value contained in this element
     * @throws ElementException If this element does not contain a string value
     */
    public String stringValue() throws ElementException {
        return valueOrThrow(String.class);
    }

    /**
     * @param def The value to return if this element does not contain a string value
     * @return The string value contained in this element
     */
    public String stringValue(String def) {
        return valueOr(String.class, def);
    }
}
