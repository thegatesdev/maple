package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;

import java.util.List;
import java.util.Map;
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

public class DataContainer extends DataElement {

    public static final DataContainer EMPTY = new DataContainer(null);

    final Object value;

    private DataContainer(Object value) {
        this.value = value;
    }

    static DataContainer read(Object data) {
        if (data == null) return null;
        final Object input;
        if (data instanceof List<?> list) {
            input = DataList.read(list);
        } else if (data instanceof Map<?, ?> map) {
            input = DataMap.readInternal(map);
        } else input = data;
        return new DataContainer(input);
    }

    @Override
    public DataContainer setName(String name) {
        super.setName(name);
        if (isValueOf(DataElement.class)) getValueOrNull(DataElement.class).setName(name);
        return this;
    }

    @Override
    public DataContainer setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public DataContainer copy() {
        return new DataContainer(value);
    }

    @Override
    public boolean isDataContainer() {
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
    public DataContainer getAsDataContainer() {
        return this;
    }

    @Override
    public DataList getAsDataList() {
        return null;
    }

    @Override
    public DataMap getAsDataMap() {
        return null;
    }

    @Override
    public String getPath() {
        return parent == null || parent.getPath() == null ? "" : parent.getPath();
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


    public boolean isValueOf(Class<?> clazz) {
        return clazz.isInstance(value);
    }


    public boolean isStringValue() {
        return value instanceof String;
    }


    public String stringValue() {
        return isStringValue() ? (String) value : null;
    }

    public String stringValue(String def) {
        return isStringValue() ? (String) value : def;
    }

    public boolean isBooleanValue() {
        return value instanceof Boolean;
    }

    public boolean booleanValue(boolean def) {
        return isBooleanValue() ? (boolean) value : def;
    }

    public boolean isNumberValue() {
        return value instanceof Number;
    }

    public int intValue(int def) {
        return isNumberValue() ? ((Number) value).intValue() : def;
    }

    public double doubleValue(double def) {
        return isNumberValue() ? ((Number) value).doubleValue() : def;
    }

    public float floatValue(float def) {
        return isNumberValue() ? ((Number) value).floatValue() : def;
    }

    public long longValue(long def) {
        return isNumberValue() ? ((Number) value).longValue() : def;
    }

    public boolean isDataElementValue() {
        return value instanceof DataElement;
    }

    public boolean isDataMapValue() {
        return value instanceof DataMap;
    }

    public boolean isDataListValue() {
        return value instanceof DataList;
    }

    public DataMap dataMapValue() {
        return value instanceof DataMap ? ((DataMap) value) : null;
    }

    public DataList dataListValue() {
        return value instanceof DataList ? ((DataList) value) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataContainer container)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(value, container.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return value == null ? "emptyContainer" : value.toString();
    }
}
