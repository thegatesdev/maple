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

    Object value;

    DataContainer() {
    }

    DataContainer(Object value) {
        setValue(value);
    }

    static DataContainer read(Object data) {
        final Object input;
        if (data instanceof List<?> list) {
            input = DataList.read(list);
        } else if (data instanceof Map<?, ?> map) {
            input = DataMap.read(map);
        } else input = data;
        return DataContainer.of(input);
    }

    public static DataContainer of(Object data) {
        if (data instanceof DataContainer container) {
            return new DataContainer(container.value);
        }
        return new DataContainer(data);
    }

    @Override
    public DataContainer setName(String name) {
        super.setName(name);
        if (isDataElement()) getAsDataElement().setName("%s".formatted(name));
        return this;
    }

    @Override
    public DataContainer setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public DataContainer copy() {
        return new DataContainer().setValue(value);
    }

    @Override
    public String getPath() {
        return parent == null ? "" : parent.getPath();
    }

    @SuppressWarnings("unchecked")
    public <T> T getUnsafe() {
        return (T) value;
    }

    public DataContainer setValue(Object value) {
        this.value = value;
        if (isDataElement()) getAsDataElement().setParent(this);
        return this;
    }

    public <T> T getOrThrow(Class<T> clazz) {
        if (isOf(clazz)) return clazz.cast(value);
        else
            throw new ReadException(this, "unexpected value type, expected " + clazz.getSimpleName() + ", got " + value.getClass().getSimpleName());
    }


    public <T> T getOrNull(Class<T> clazz) {
        return isOf(clazz) ? clazz.cast(value) : null;
    }

    public boolean isPresent() {
        return value != null;
    }


    public boolean isOf(Class<?> clazz) {
        return clazz.isInstance(value);
    }

    public boolean isDataList() {
        return value instanceof DataList;
    }


    public DataList getAsDataList() {
        return isDataList() ? (DataList) value : null;
    }

    public boolean isDataMap() {
        return value instanceof DataMap;
    }


    public DataMap getAsDataMap() {
        return isDataMap() ? ((DataMap) value) : null;
    }

    boolean isDataElement() {
        return value instanceof DataElement;
    }

    DataElement getAsDataElement() {
        return isDataElement() ? (DataElement) value : null;
    }


    public boolean isString() {
        return value instanceof String;
    }


    public String getAsString() {
        return isString() ? (String) value : null;
    }

    public String getAsString(String def) {
        return isString() ? (String) value : def;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean getAsBoolean(boolean def) {
        return isBoolean() ? (boolean) value : def;
    }

    public int getAsInt(int def) {
        return isNumber() ? ((Number) value).intValue() : def;
    }

    public double getAsDouble(double def) {
        return isNumber() ? ((Number) value).doubleValue() : def;
    }

    public float getAsFloat(float def) {
        return isNumber() ? ((Number) value).floatValue() : def;
    }

    public long getAsLong(long def) {
        return isNumber() ? ((Number) value).longValue() : def;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }


    public List<String> getAsStringList() {
        if (!isDataList()) return null;
        return getAsDataList().getAsListOf(String.class);
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
}
