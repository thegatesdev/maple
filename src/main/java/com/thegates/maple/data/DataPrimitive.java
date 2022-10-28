package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;
import com.thegates.maple.exception.RequireTypeException;

import java.util.Objects;

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

public class DataPrimitive extends DataElement {

    final Object value;

    public DataPrimitive(Object value) {
        this.value = value;
        if (value instanceof DataElement e) {
            e.setParent(this);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getValueUnsafe() {
        return (T) value;
    }

    public <T> T getValueOrThrow(Class<T> clazz) {
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
        return value == null;
    }


    public <T> T requireValue(Class<T> clazz) throws RequireTypeException {
        if (!isValueOf(clazz)) throw new RequireTypeException(this, clazz);
        return getValueUnsafe();
    }

    private <T> T throwRequire(String typeName) {
        throw new RequireTypeException(this, typeName);
    }

    public boolean isValueOf(Class<?> clazz) {
        return clazz.isInstance(value);
    }


    public boolean isStringValue() {
        return value instanceof String;
    }

    public String stringValue() {
        return requireValue(String.class);
    }

    public boolean isBooleanValue() {
        return value instanceof Boolean;
    }

    public boolean booleanValue() {
        return isBooleanValue() ? (boolean) value : throwRequire("boolean");
    }

    public boolean isNumberValue() {
        return value instanceof Number;
    }

    public int intValue() {
        return isNumberValue() ? ((Number) value).intValue() : throwRequire("number");
    }

    public double doubleValue() {
        return isNumberValue() ? ((Number) value).doubleValue() : throwRequire("number");
    }

    public float floatValue() {
        return isNumberValue() ? ((Number) value).floatValue() : throwRequire("number");
    }

    public long longValue() {
        return isNumberValue() ? ((Number) value).longValue() : throwRequire("number");
    }


    // --


    @Override
    public DataPrimitive setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public DataPrimitive setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public DataPrimitive copy() {
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
        if (!(o instanceof DataPrimitive container)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(value, container.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return value == null ? "emptyContainer" : "dataContainer with " + value;
    }
}
