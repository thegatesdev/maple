package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.exception.ElementException;

import java.util.function.Consumer;
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

    private final Class<Value> valueType;

    /**
     * @param valueType The type of the value contained in this DataValue.
     */
    private DataValue(Class<Value> valueType) {
        this.valueType = valueType;
    }

    /**
     * @return The type of the value contained in this DataValue.
     */
    public Class<Value> valueType() {
        return valueType;
    }

    // -- VALUE

    /**
     * @param expectedType The class the value should be an instance of.
     * @param <T>          The type to get the value as.
     * @return The cast value, or {@code null} if the value is not an instance of {@code expectedType}.
     */
    public <T> T valueOrNull(Class<T> expectedType) {
        return valueOf(expectedType) ? valueUnsafe() : null;
    }

    /**
     * @param expectedType The class the value should be an instance of.
     * @param <T>          The type to get the value as.
     * @param def          The default value.
     * @return The cast value, or the specified default value if the value is not an instance of {@code expectedType}.
     */
    public <T> T valueOr(Class<T> expectedType, T def) {
        var value = valueOrNull(expectedType);
        return value == null ? def : value;
    }

    /**
     * @param clazz The class the value should be of.
     * @param <T>   The type the value should be of.
     * @return The cast value.
     * @throws ElementException If the value is not an instance of {@code clazz}.
     */
    public <T> T valueOrThrow(Class<T> clazz) throws ElementException {
        requireType(clazz);
        return valueUnsafe();
    }

    /**
     * Throws if this element does not contain a value of the specified type.
     */
    @SuppressWarnings("unchecked")
    public <T> DataValue<T> requireType(Class<T> clazz) throws ElementException {
        if (!valueOf(clazz)) throw ElementException.requireType(this, clazz);
        return (DataValue<T>) this;
    }

    /**
     * @param expectedType The class the value should conform to.
     * @return True if the value of this primitive conforms to clazz
     */
    public boolean valueOf(Class<?> expectedType) {
        return expectedType.isAssignableFrom(valueType);
    }

    /**
     * Unsafely casts the value to {@code Value}.
     *
     * @param <T> The type to cast the value to.
     * @return The cast value.
     */
    @SuppressWarnings("unchecked")
    public <T> T valueUnsafe() {
        return (T) raw();
    }

    /**
     * Creates a new dynamic or static DataValue using the supplier.
     * If this element is static, it will create a new static value by applying the specified supplier.
     * If this element is dynamic, it will create a new dynamic value that applies the specified supplier everytime the value is gotten.
     *
     * @param valueType The type of the new DataValue.
     * @param supplier  The value supplier.
     * @param <T>       The type of the new DataValue.
     * @return A new DataValue.
     */
    public abstract <T> DataValue<T> andThen(Class<T> valueType, Supplier<T> supplier);

    // PRIMITIVE GETTERS

    /**
     * @return The boolean value contained in this element.
     * @throws ElementException If this element does not contain a boolean value.
     */
    public Boolean booleanValue() throws ElementException {
        return valueOrThrow(Boolean.class);
    }

    /**
     * @param def The value to return if this element does not contain a boolean value.
     * @return The boolean value contained in this element.
     */
    public Boolean booleanValue(boolean def) {
        return valueOr(Boolean.class, def);
    }

    /**
     * @return The integer value contained in this element.
     * @throws ElementException If this element does not contain a integer value.
     */
    public Integer intValue() throws ElementException {
        return valueOrThrow(Integer.class);
    }

    /**
     * @param def The value to return if this element does not contain an integer value.
     * @return The integer value contained in this element.
     */
    public Integer intValue(int def) {
        return valueOr(Integer.class, def);
    }

    /**
     * @return The string value contained in this element.
     * @throws ElementException If this element does not contain a string value.
     */
    public String stringValue() throws ElementException {
        return valueOrThrow(String.class);
    }

    /**
     * @param def The value to return if this element does not contain a string value.
     * @return The string value contained in this element.
     */
    public String stringValue(String def) {
        return valueOr(String.class, def);
    }

    // -- ELEMENT


    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public DataValue<Value> asValue() throws UnsupportedOperationException {
        return this;
    }

    @Override
    public void ifValue(Consumer<DataValue<?>> valueConsumer, Runnable elseAction) {
        valueConsumer.accept(this);
    }

    @Override
    public Object view() {
        return raw();
    }

    @Override
    public String toString() {
        return "value<" + valueType.getSimpleName() + ">";
    }

    @Override
    public abstract DataValue<Value> shallowCopy();

    @Override
    public abstract DataValue<Value> deepCopy();

    @Override
    public int hashCode() {
        int result = friendlyName().hashCode();
        result = 31 * result + (valueType().hashCode());
        return result;
    }

    /**
     * A static value holder.
     */
    public static class Static<Value> extends DataValue<Value> {
        private final Value value;

        /**
         * Construct a new static DataValue containing the specified value.
         */
        @SuppressWarnings("unchecked")
        public Static(Value value) {
            super((Class<Value>) value.getClass());
            this.value = value;
        }

        @Override
        public <T> DataValue<T> andThen(Class<T> valueType, Supplier<T> supplier) {
            return new Static<>(supplier.get());
        }

        @Override
        protected Object raw() {
            return value;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public DataValue<Value> shallowCopy() {
            return new Static<>(value);
        }

        @Override
        public DataValue<Value> deepCopy() {
            return shallowCopy();
        }
    }

    /**
     * A changing value holder.
     */
    public static class Dynamic<Value> extends DataValue<Value> {
        private final Supplier<Value> valueSupplier;

        /**
         * Construct a new static DataValue of the specified type using the specified supplier.
         */
        public Dynamic(Class<Value> valueType, Supplier<Value> valueSupplier) {
            super(valueType);
            this.valueSupplier = valueSupplier;
        }

        @Override
        public <D> DataValue<D> andThen(Class<D> valueType, Supplier<D> supplier) {
            return new Dynamic<>(valueType, supplier);
        }

        @Override
        protected Object raw() {
            return valueSupplier.get();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public DataValue<Value> shallowCopy() {
            return new Dynamic<>(valueType(), valueSupplier);
        }

        @Override
        public DataValue<Value> deepCopy() {
            return shallowCopy();
        }
    }
}
