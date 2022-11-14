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
 * A DataElement has a parent and name that can only be set once, in the constructor or using {@link DataElement#setData(DataElement, String)}.
 * This is to reduce the amount of copying elements when for example inserting in a {@link DataMap}, where the name and parent have to be set by the parent.
 */

public abstract class DataElement implements Cloneable, Comparable<DataElement> {

    protected static final Object MODIFY_MUTEX = new Object();
    protected static final Object READ_MUTEX = new Object();

    private final Class<? extends DataElement> type = getClass();

    private DataElement parent;
    private String name;
    private boolean dataSet = false;

    /**
     * Constructs a new DataElement with the data not being set.
     */
    protected DataElement() {
    }

    /**
     * Constructs a new DataElement.
     * If the name is {@code null} the element data will not be set. Should've called {@link DataElement#DataElement()}.
     *
     * @param name The name of this element.
     */
    protected DataElement(String name) {
        this(null, name);
    }

    /**
     * Constructs a new DataElement.
     * If both the name and parent are {@code null} the element data will not be set. Should've called {@link DataElement#DataElement()}.
     *
     * @param name   The name of this element.
     * @param parent The parent of this element.
     */
    protected DataElement(DataElement parent, String name) {
        if (parent != null || name != null)
            setData(parent, name);
    }

    public static DataElement readOf(Object input) {
        if (input == null) return new DataNull();
        final Object reading = (input instanceof DataElement el) ? el.getValue() : input;
        if (reading instanceof Map<?, ?> map) return DataMap.readInternal(map);
        if (reading instanceof List<?> list) return DataList.read(list);
        return new DataPrimitive(reading);
    }

    DataElement setData(DataElement parent, String name) {
        if (dataSet) throw new IllegalArgumentException("Parent and name already set");
        dataSet = true;
        this.parent = parent;
        this.name = name;
        return this;
    }

    public boolean hasDataSet() {
        return dataSet;
    }

    public abstract boolean isEmpty();

    public boolean isPresent() {
        return !isEmpty();
    }

    public abstract DataElement clone();

    @Override
    public int compareTo(DataElement o) {
        if (this.hasParent(o)) return -1;
        if (this == o.parent) return 1;
        return 0;
    }

    public boolean hasParent(DataElement parent) {
        if (this.parent == null) return false;
        if (this.parent == parent) return true;
        return this.parent.hasParent(parent);
    }

    public abstract Object getValue();


    protected abstract Object value();

    public abstract boolean isDataPrimitive();

    public abstract boolean isDataList();

    public abstract boolean isDataMap();

    public abstract boolean isDataNull();


    public boolean isOf(Class<? extends DataElement> elementClass) {
        return type == elementClass;
    }


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


    public DataElement findRoot() {
        if (parent == null) return this;
        return parent.findRoot();
    }

    public String path() {
        final String n = name == null ? "root" : name;
        return parent == null ? n : parent.path() + "." + n;
    }

    public DataElement parent() {
        return parent;
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataElement that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(value(), that.value());
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value() != null ? value().hashCode() : 0);
        return result;
    }
}
