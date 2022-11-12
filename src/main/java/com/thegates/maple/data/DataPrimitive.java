package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;
import com.thegates.maple.exception.RequireTypeException;

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
    private String cachedSimpleName = null;

    public DataPrimitive(Object value) {
        setValue(value);
    }

    public DataPrimitive(String name, Object value) {
        super(name);
        setValue(value);
    }

    protected DataPrimitive(DataElement parent, String name, Object value) {
        super(parent, name);
        setValue(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
        cachedSimpleName = value.getClass().getSimpleName();
    }

    @Override
    protected Object value() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueUnsafe() {
        return (T) value;
    }

    public <T> T getValueOrThrow(Class<T> clazz) throws ReadException {
        if (isValueOf(clazz)) return clazz.cast(value);
        else
            throw new ReadException(this, "unexpected value type, expected " + clazz.getSimpleName() + ", got " + value.getClass().getSimpleName());
    }

    public <T> T getValueOrNull(Class<T> clazz) {
        return isValueOf(clazz) ? clazz.cast(value) : null;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean isEmpty() {
        return !isPresent();
    }


    public <T> T requireValue(Class<T> clazz) throws RequireTypeException {
        if (!isValueOf(clazz)) throw new RequireTypeException(this, clazz);
        return getValueUnsafe();
    }

    public boolean isValueOf(Class<?> clazz) {
        if (isEmpty()) return false;
        return clazz.isInstance(value);
    }


    public boolean isStringValue() {
        return value instanceof String;
    }

    public String stringValue() throws RequireTypeException {
        return requireValue(String.class);
    }

    public boolean isBooleanValue() {
        return value instanceof Boolean;
    }

    public boolean booleanValue() throws RequireTypeException {
        return requireValue(Boolean.class);
    }

    public boolean isNumberValue() {
        return value instanceof Number;
    }

    public int intValue() throws RequireTypeException {
        return requireValue(Number.class).intValue();
    }

    public double doubleValue() throws RequireTypeException {
        return requireValue(Number.class).intValue();
    }

    public float floatValue() throws RequireTypeException {
        return requireValue(Number.class).floatValue();
    }

    public long longValue() throws RequireTypeException {
        return requireValue(Number.class).longValue();
    }


    // --

    @Override
    public DataPrimitive clone() {
        return new DataPrimitive(value);
    }

    @Override
    public boolean isDataPrimitive() {
        return true;
    }

    @Override
    public boolean isDataList() {
        return false;
    }

    @Override
    public boolean isDataMap() {
        return false;
    }

    @Override
    public boolean isDataNull() {
        return false;
    }

    @Override
    public DataPrimitive getAsDataPrimitive() {
        return this;
    }


    // --


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
