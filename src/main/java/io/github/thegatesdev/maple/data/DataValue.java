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
public abstract class DataValue extends DataElement {

    private final Class<?> valueType;

    /**
     * @param valueType The type of the value contained in this DataValue.
     */
    private DataValue(Class<?> valueType) {
        this.valueType = valueType;
    }

    /**
     * @return The type of the value contained in this DataValue.
     */
    public Class<?> valueType() {
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
     * @param clazz The class the value should be of.
     * @param <T>   The type the value should be of.
     * @return The cast value.
     * @throws ElementException If the value is not an instance of {@code clazz}.
     */
    public <T> T valueOrThrow(Class<T> clazz) throws ElementException {
        if (!valueOf(clazz)) throw ElementException.requireType(this, clazz);
        return valueUnsafe();
    }

    /**
     * @param expectedType The class the value should conform to.
     * @return True if the value of this primitive conforms to clazz
     */
    public boolean valueOf(Class<?> expectedType) {
        return expectedType.isAssignableFrom(valueType);
    }

    /**
     * Unsafely casts the value to {@code T}.
     *
     * @param <T> The type to cast the value to.
     * @return The cast value.
     */
    @SuppressWarnings("unchecked")
    public <T> T valueUnsafe() {
        return (T) raw();
    }


    // -- ELEMENT


    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public DataValue asValue() throws UnsupportedOperationException {
        return this;
    }

    @Override
    public void ifValue(Consumer<DataValue> valueConsumer, Runnable elseAction) {
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

    static class Static extends DataValue {
        private final Object value;

        Static(Object value) {
            super(value.getClass());
            this.value = value;
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
        public DataElement shallowCopy() {
            return new Static(value);
        }

        @Override
        public DataElement deepCopy() {
            return shallowCopy();
        }
    }

    static class Dynamic<T> extends DataValue {
        private final Supplier<T> valueSupplier;

        Dynamic(Class<T> valueType, Supplier<T> valueSupplier) {
            super(valueType);
            this.valueSupplier = valueSupplier;
        }

        @Override
        protected Object raw() {
            return valueSupplier.get();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public DataElement shallowCopy() {
            return new Dynamic<>((Class<T>) valueType(), valueSupplier);
        }

        @Override
        public DataElement deepCopy() {
            return shallowCopy();
        }
    }
}
