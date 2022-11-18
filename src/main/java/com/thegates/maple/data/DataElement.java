package com.thegates.maple.data;

import com.thegates.maple.exception.ElementException;

import java.util.Collection;
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
 * An abstract class for any element.
 * A DataElement contains a parent and name, which together are only settable once, using a constructor, or dedicated methods {@link DataElement#setData(DataElement, String)} and {@link DataElement#setName(String)}.
 * With the name and parent it can calculate the root of the structure ({@link DataElement#root()}), the path to this element ({@link DataElement#path()}) or check if it is a child of an element ({@link DataElement#isChild(DataElement)}).
 * The parent is only to be set by the parent itself, to avoid 'ghost' elements that have the parent set, but are not actually contained in the structure.
 */
public abstract class DataElement implements Cloneable, Comparable<DataElement> {

    protected static final Object MODIFY_MUTEX = new Object();
    protected static final Object READ_MUTEX = new Object();

    private final Class<? extends DataElement> cachedType = getClass();
    private String cachedPath;

    private DataElement parent;
    private String name;
    private boolean dataSet = false;

    /**
     * Constructs a DataElement with its data unset.
     */
    protected DataElement() {
    }

    /**
     * Constructs a DataElement with its parent defaulted to {@code null}.
     *
     * @param name The name to initialize the data with.
     */
    protected DataElement(String name) {
        setData(null, name);
    }

    /**
     * Read this object as a DataElement, so that when;
     * <ul>
     * <li>{@code input == null} -> {@link DataNull#DataNull()}.
     * <li>{@code input instanceof Collection<?>} -> {@link DataList#read(Collection)}.
     * <li>{@code input instanceof Map<?,?>} -> {@link DataMap#read(Map)}.
     * <li>If none of the above apply -> {@link DataPrimitive#DataPrimitive(Object)}.
     * </ul>
     *
     * @param input The Object to read from.
     * @return The new DataNull, DataList, DataMap or DataPrimitive.
     */
    public static DataElement readOf(Object input) {
        if (input == null) return new DataNull();
        final Object reading = (input instanceof DataElement el) ? el.value() : input;
        if (reading instanceof Map<?, ?> map) return DataMap.readInternal(map);
        if (reading instanceof Collection<?> collection) return DataList.read(collection);
        return new DataPrimitive(reading);
    }

    /**
     * Sets the data.
     *
     * @param parent The parent to initialize the data with.
     * @param name   The name to initialize the data with.
     * @return The same DataElement.
     * @throws IllegalArgumentException When the data is already set.
     */
    DataElement setData(DataElement parent, String name) throws IllegalArgumentException {
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

    /**
     * Get the cached path of the element.
     *
     * @return The path of this element as a dot separated string.
     */
    public String path() {
        return cachedPath;
    }

    /**
     * @return The value contained in this DataElement.
     */
    
    public abstract Object value();

    /**
     * @return This element as a DataList.
     * @throws UnsupportedOperationException If this element is not a DataList.
     */
    public DataList asList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a list!");
    }

    /**
     * @return This element as a DataMap.
     * @throws UnsupportedOperationException If this element is not a DataMap.
     */
    public DataMap asMap() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a map!");
    }

    /**
     * @param elementClass The class to get this DataElement as.
     * @return This element cast to E, or null if the element could not be cast.
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E asOrNull(Class<E> elementClass) {
        if (isOf(elementClass)) return (E) this;
        return null;
    }

    /**
     * @param elementClass The class to check this DataElement for.
     */
    public boolean isOf(Class<? extends DataElement> elementClass) {
        return cachedType == elementClass;
    }

    /**
     * @return This element as a DataPrimitive.
     * @throws UnsupportedOperationException If this element is not a DataPrimitive.
     */
    public DataPrimitive asPrimitive() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a primitive!");
    }

    /**
     * Cast to E
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E asUnsafe(Class<E> elementClass) {
        return (E) this;
    }

    /**
     * @return {@code true} if this element's name is not {@code null}
     */
    public boolean hasName() {
        return name != null;
    }

    /**
     * Check if {@code parent} is in the chain of parents of this DataElement.
     *
     * @param parent The parent to check for.
     * @return {@code true} if the parent was found.
     */
    public boolean isChild(DataElement parent) {
        if (this.parent == null) return false;
        if (this.parent == parent) return true;
        return this.parent.isChild(parent);
    }

    /**
     * Check if this elements parent is not equal to {@code null}.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Check if the data (parent / name) is initialized.
     * Initializing the data can be done through a constructor or {@link DataElement#setData(DataElement, String)}
     */
    public boolean isDataSet() {
        return dataSet;
    }

    /**
     * Check if this DataElement is a DataList.
     */
    public abstract boolean isList();

    /**
     * Check if this DataElement is a DataMap.
     */
    public abstract boolean isMap();

    /**
     * Check if this DataElement is a DataNull.
     */
    public abstract boolean isNull();

    /**
     * Check if this DataElement's value is not empty.
     */
    public boolean isPresent() {
        return !isEmpty();
    }

    /**
     * Check if this DataElement's value empty. Implementation may differ.
     */
    public abstract boolean isEmpty();

    /**
     * Check if this DataElement is a DataPrimitive.
     */
    public abstract boolean isPrimitive();

    /**
     * @return The name of this element.
     */
    public String name() {
        return name;
    }

    /**
     * @return The parent of this element.
     */
    public DataElement parent() {
        return parent;
    }

    /**
     * @param elementClass The class this element is required to be.
     * @param <T>          The type of {@code elementClass}.
     * @return This element, casted to T.
     * @throws ElementException If this element is not assignable to {@code elementClass}.
     */
    @SuppressWarnings("unchecked")
    public <T extends DataElement> T requireOf(Class<T> elementClass) throws ElementException {
        if (!isOf(elementClass)) throw ElementException.requireType(this, elementClass);
        return ((T) this);
    }

    /**
     * Find the root parent of this element.
     *
     * @return The root parent. This may be the same as the element it is called on.
     */
    public DataElement root() {
        if (parent == null) return this;
        return parent.root();
    }

    /**
     * Set the name of this element.
     *
     * @throws IllegalArgumentException When the data is already set.
     */
    public DataElement setName(String name) throws IllegalArgumentException {
        return setData(null, name);
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

    /**
     * Clone this element.
     *
     * @return A new DataElement of the original type, <b>without it's data set.</b>
     */
    public abstract DataElement clone();

    /**
     * @return The raw value contained in this element.
     */
    protected abstract Object raw();
}
