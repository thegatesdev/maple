package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;

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

public class DataPrimitive extends DataElement implements Cloneable, Comparable<DataElement> {

    Object value;
    private String cachedSimpleName;

    public DataPrimitive() {
    }

    public DataPrimitive(String name, Object value) {
        super(name);
        setValue(value);
    }

    public DataPrimitive(Object value) {
        setValue(value);
    }

    public Object value() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        cachedSimpleName = value.getClass().getSimpleName();
    }

    @Override
    protected Object raw() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> T valueUnsafe() {
        return (T) value;
    }

    public <T> T valueOrThrow(Class<T> clazz) throws ReadException {
        if (isValueOf(clazz)) return clazz.cast(value);
        else
            throw new ReadException(this, "unexpected value type, expected " + clazz.getSimpleName() + ", got " + cachedSimpleName);
    }

    public <T> T valueOrNull(Class<T> clazz) {
        return isValueOf(clazz) ? clazz.cast(value) : null;
    }

    public boolean isEmpty() {
        return value == null;
    }


    public <T> T requireValue(Class<T> clazz) throws ReadException {
        if (!isValueOf(clazz)) throw ReadException.requireType(this, clazz);
        return valueUnsafe();
    }

    public boolean isValueOf(Class<?> clazz) {
        if (isEmpty()) return false;
        return clazz.isInstance(value);
    }


    public boolean isStringValue() {
        return value instanceof String;
    }

    public String stringValue() throws ReadException {
        return requireValue(String.class);
    }

    public boolean isBooleanValue() {
        return value instanceof Boolean;
    }

    public boolean booleanValue() throws ReadException {
        return requireValue(Boolean.class);
    }

    public boolean isNumberValue() {
        return value instanceof Number;
    }

    public int intValue() throws ReadException {
        return requireValue(Number.class).intValue();
    }

    public double doubleValue() throws ReadException {
        return requireValue(Number.class).intValue();
    }

    public float floatValue() throws ReadException {
        return requireValue(Number.class).floatValue();
    }

    public long longValue() throws ReadException {
        return requireValue(Number.class).longValue();
    }


    @Override
    public DataPrimitive clone() {
        return new DataPrimitive(value);
    }

    @Override
    public boolean isPrimitive() {
        return true;
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
    public DataPrimitive asPrimitive() {
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataPrimitive)) return false;
        return super.equals(o);
    }

    @Override
    public String toString() {
        return value == null ? "nullPrimitive" : "dataPrimitive<" + cachedSimpleName + ">";
    }
}
