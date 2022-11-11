package com.thegates.maple.data;

import com.thegates.maple.exception.RequireTypeException;

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

/**
 * A base class for any element of data.
 * A DataElement has an immutable parent and name, and inserting in e.g. a DataList requires the element to be copied using {@link DataElement#copy(DataElement, String)}
 */

public abstract class DataElement {

    protected static final Object MODIFY_MUTEX = new Object();
    protected static final Object GET_MUTEX = new Object();

    private final DataElement parent;
    private final String name;

    /**
     * Constructs a new DataElement with the parent set to {@code null} and the name set to {@code "root"}.
     */
    protected DataElement() {
        this("root");
    }

    /**
     * Constructs a new DataElement with the parent set to {@code null}.
     *
     * @param name The name of this element.
     */
    protected DataElement(String name) {
        this(null, name);
    }

    /**
     * Constructs a new DataElement.
     *
     * @param name   The name of this element.
     * @param parent The parent of this element.
     */
    protected DataElement(DataElement parent, String name) {
        this.parent = parent;
        if (name == null) throw new NullPointerException("'name' cannot be null");
        this.name = name;
    }

    public static DataElement readOf(Object input) {
        if (input == null) return new DataNull();
        final Object reading = (input instanceof DataElement el) ? el.getValue() : input;
        if (reading instanceof Map<?, ?> map) return DataMap.readInternal(map);
        if (reading instanceof List<?> list) return DataList.read(list);
        return new DataPrimitive(reading);
    }

    public abstract DataElement copy(DataElement parent, String name);


    public abstract Object getValue();

    public abstract boolean isDataPrimitive();

    public abstract boolean isDataList();

    public abstract boolean isDataMap();

    public abstract boolean isDataNull();


    public abstract boolean isOf(Class<? extends DataElement> elementClass);


    @SuppressWarnings("unchecked")
    public <T extends DataElement> T requireOf(Class<T> elementClass) throws RequireTypeException {
        if (!isOf(elementClass)) throw new RequireTypeException(this, elementClass);
        return ((T) this);
    }


    public DataPrimitive getAsDataPrimitive() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a primitive!");
    }

    public DataList getAsDataList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a list!");
    }

    public DataMap getAsDataMap() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a map!");
    }


    @SuppressWarnings("unchecked")
    public <T extends DataElement> T getAsOrNull(Class<T> elementClass) {
        if (isOf(elementClass)) return (T) this;
        return null;
    }


    public String getPath() {
        final String n = name == null ? "" : name;
        return parent == null ? n : parent.getPath() + "." + n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataElement that)) return false;
        return Objects.equals(parent, that.parent) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }
}
