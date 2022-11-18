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

public abstract class DataElement implements Cloneable, Comparable<DataElement> {

    protected static final Object MODIFY_MUTEX = new Object();
    protected static final Object READ_MUTEX = new Object();

    private final Class<? extends DataElement> cachedType = getClass();
    private String cachedPath;

    private DataElement parent;
    private String name;
    private boolean dataSet = false;

    protected DataElement() {
    }

    protected DataElement(String name) {
        setData(null, name);
    }

    DataElement setData(DataElement parent, String name) {
        if (dataSet) throw new IllegalArgumentException("Parent and name already set");
        dataSet = true;
        this.parent = parent;
        this.name = name;
        cachedPath = calcPath();
        return this;
    }

    private String calcPath() {
        final String n = name == null ? "root" : name;
        return parent == null ? n : parent.path() + "." + n;
    }

    public String path() {
        return cachedPath;
    }

    public static DataElement readOf(Object input) {
        if (input == null) return new DataNull();
        final Object reading = (input instanceof DataElement el) ? el.value() : input;
        if (reading instanceof Map<?, ?> map) return DataMap.readInternal(map);
        if (reading instanceof List<?> list) return DataList.read(list);
        return new DataPrimitive(reading);
    }

    public abstract Object value();

    public DataList asList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a list!");
    }

    public DataMap asMap() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a map!");
    }

    @SuppressWarnings("unchecked")
    public <E extends DataElement> E asOrNull(Class<E> elementClass) {
        if (isOf(elementClass)) return (E) this;
        return null;
    }

    public boolean isOf(Class<? extends DataElement> elementClass) {
        return cachedType == elementClass;
    }

    public DataPrimitive asPrimitive() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a primitive!");
    }

    @SuppressWarnings("unchecked")
    public <E extends DataElement> E asUnsafe(Class<E> elementClass) {
        return (E) this;
    }

    public boolean hasName() {
        return name != null;
    }

    public boolean hasParent(DataElement parent) {
        if (this.parent == null) return false;
        if (this.parent == parent) return true;
        return this.parent.hasParent(parent);
    }

    public boolean hasParent() {
        return parent != null;
    }

    public boolean isDataSet() {
        return dataSet;
    }

    public abstract boolean isList();

    public abstract boolean isMap();

    public abstract boolean isNull();

    public boolean isPresent() {
        return !isEmpty();
    }

    public abstract boolean isEmpty();

    public abstract boolean isPrimitive();

    public String name() {
        return name;
    }

    public DataElement parent() {
        return parent;
    }

    @SuppressWarnings("unchecked")
    public <T extends DataElement> T requireOf(Class<T> elementClass) throws ReadException {
        if (!isOf(elementClass)) throw ReadException.requireType(this, elementClass);
        return ((T) this);
    }

    public DataElement root() {
        if (parent == null) return this;
        return parent.root();
    }

    @Override
    public int compareTo(DataElement o) {
        return name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (raw() != null ? raw().hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataElement that)) return false;
        System.out.printf("EQUALS [%s] name:%s raw:%s%n", cachedPath, Objects.equals(name, that.name), Objects.equals(raw(), that.raw()));
        return Objects.equals(name, that.name) && Objects.equals(raw(), that.raw());
    }

    public abstract DataElement clone();

    protected abstract Object raw();
}
