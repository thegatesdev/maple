package com.thegates.maple.data;

import com.thegates.maple.exception.ElementException;

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
 * A primitive element. It holds an Object, and has methods to check the type, get the value as etc.
 * A DataPrimitive's value is mutable, unlike the other DataElement implementations.
 */
public class DataPrimitive extends DataElement implements Cloneable, Comparable<DataElement> {

    Object value;
    private String cachedSimpleName;

    /**
     * Constructs an empty DataList with its data unset.
     */
    public DataPrimitive() {
    }

    /**
     * Constructs a DataPrimitive with its parent defaulted to {@code null}.
     *
     * @param name  The name to initialize the data with.
     * @param value The value to hold.
     */
    public DataPrimitive(String name, Object value) {
        super(name);
        value(value);
    }

    /**
     * Constructs a DataPrimitive with its data unset.
     * The name only constructor is left out to avoid confusion with this constructor. Use {@link DataElement#name(String)} to set the name instead.
     *
     * @param value The value to hold.
     */
    public DataPrimitive(Object value) {
        value(value);
    }

    /**
     * Sets this primitives value.
     *
     * @param value The value to set to.
     */
    public void value(Object value) {
        this.value = value;
        cachedSimpleName = value.getClass().getSimpleName();
    }

    /**
     * @return The boolean this primitive holds.
     * @throws ElementException If the value this primitive holds is not a boolean.
     */
    public boolean booleanValue() throws ElementException {
        return requireValue(Boolean.class);
    }

    /**
     * @param clazz The class the value should be of.
     * @param <T>   The type the value should be of.
     * @return The cast value.
     * @throws ElementException If the value is not an instance of {@code clazz}.
     */
    public <T> T requireValue(Class<T> clazz) throws ElementException {
        if (!valueOf(clazz)) throw ElementException.requireType(this, clazz);
        return valueUnsafe();
    }

    /**
     * Check if the value contained is an instance of {@code clazz}.
     */
    public boolean valueOf(Class<?> clazz) {
        if (isEmpty()) return false;
        return clazz.isInstance(value);
    }

    /**
     * @param <T> The type to cast the value to.
     * @return The cast value.
     */
    @SuppressWarnings("unchecked")
    public <T> T valueUnsafe() {
        return (T) value;
    }

    /**
     * @return The double this primitive holds.
     * @throws ElementException If the value this primitive holds is not a number.
     */
    public double doubleValue() throws ElementException {
        return requireValue(Number.class).intValue();
    }

    /**
     * @return The float this primitive holds.
     * @throws ElementException If the value this primitive holds is not a number.
     */
    public float floatValue() throws ElementException {
        return requireValue(Number.class).floatValue();
    }

    /**
     * @return The int this primitive holds.
     * @throws ElementException If the value this primitive holds is not a number.
     */
    public int intValue() throws ElementException {
        return requireValue(Number.class).intValue();
    }

    /**
     * Check if the value contained is a boolean.
     *
     * @return {@code true} if the value is a boolean.
     */
    public boolean isBooleanValue() {
        return value instanceof Boolean;
    }

    /**
     * Check if the value contained is a number.
     *
     * @return {@code true} if the value is a number.
     */
    public boolean isNumberValue() {
        return value instanceof Number;
    }

    /**
     * Check if the value contained is a String.
     *
     * @return {@code true} if the value is a String
     */
    public boolean isStringValue() {
        return value instanceof String;
    }

    /**
     * @return The long this primitive holds.
     * @throws ElementException If the value this primitive holds is not a number.
     */
    public long longValue() throws ElementException {
        return requireValue(Number.class).longValue();
    }

    /**
     * @return The String this primitive holds.
     * @throws ElementException If the value this primitive holds is not a String.
     */
    public String stringValue() throws ElementException {
        return requireValue(String.class);
    }

    /**
     * @param clazz The class the value should be an instance of.
     * @param <T>   The type to get the value as.
     * @return The cast value, or {@code null} if the value is not an instance of {@code clazz}.
     */
    public <T> T valueOrNull(Class<T> clazz) {
        return valueOf(clazz) ? clazz.cast(value) : null;
    }

    @Override
    public String toString() {
        return value == null ? "nullPrimitive" : "dataPrimitive<" + cachedSimpleName + ">";
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public DataPrimitive asPrimitive() {
        return this;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    protected Object raw() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataPrimitive)) return false;
        return super.equals(o);
    }

    @Override
    public DataPrimitive clone() {
        return new DataPrimitive(value);
    }
}
