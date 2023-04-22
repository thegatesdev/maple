package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.exception.ElementException;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

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
 * A DataElement contains a parent and name, which together are only settable once, using a constructor, or dedicated methods {@link DataElement#setData(DataElement, String)} and {@link DataElement#name(String)}.
 * With the name and parent it can calculate the root of the structure ({@link DataElement#root()}), the path to this element ({@link DataElement#path()}) or check if it is a child of an element ({@link DataElement#isChild(DataElement)}).
 * The parent is only to be set by the parent itself, to avoid 'ghost' elements that have the parent set, but are not actually contained in the structure.
 */
public abstract class DataElement implements Cloneable, Comparable<DataElement> {
    private final Class<? extends DataElement> cachedType = getClass();
    private String[] cachedPath;

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
        return this;
    }

    /**
     * Read this object as a DataElement, so that when;
     * <ul>
     * <li>{@code input == null} -> {@link DataNull#DataNull()}.
     * <li>{@code input instanceof Collection<?>} -> {@link DataList#read(Iterable)}.
     * <li>{@code input instanceof Map<?,?>} -> {@link DataMap#read(Map)}.
     * <li>{@code input instanceof Object[]} -> {@link DataArray#read(Object[])}.
     * <li>If none of the above apply -> {@link DataPrimitive#DataPrimitive(Object)}.
     * </ul>
     *
     * @param input The Object to read from.
     * @return The new DataNull, DataList, DataArray, DataMap or DataPrimitive.
     */
    public static DataElement readOf(Object input) {
        if (input == null) return new DataNull();
        final Object reading = (input instanceof DataElement el) ? el.value() : input;
        if (reading instanceof Map<?, ?> map) return DataMap.readUnknown(map);
        if (reading instanceof Iterable<?> collection) return DataList.read(collection);
        if (reading instanceof Object[] array) return DataArray.read(array);
        return new DataPrimitive(reading);
    }

    /**
     * Get the value contained in this DataElement.
     *
     * @return The value contained in this DataElement.
     */

    public abstract Object value();

    /**
     * Get this element as a DataArray, or throw.
     *
     * @return This element as a DataArray.
     * @throws UnsupportedOperationException If this element is not a DataArray.
     */
    public DataArray asArray() {
        throw new UnsupportedOperationException("Not an array!");
    }

    /**
     * Get this element as a DataList, or throw.
     *
     * @return This element as a DataList.
     * @throws UnsupportedOperationException If this element is not a DataList.
     */
    public DataList asList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a list!");
    }

    /**
     * Get this element as a DataMap, or throw.
     *
     * @return This element as a DataMap.
     * @throws UnsupportedOperationException If this element is not a DataMap.
     */
    public DataMap asMap() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a map!");
    }

    /**
     * Get this element as E, or return null.
     *
     * @param elementClass The class to get this DataElement as.
     * @param <E>          The type to get this DataElement as.
     * @return This element cast to E, or null if the element could not be cast.
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E asOrNull(Class<E> elementClass) {
        if (isOf(elementClass)) return (E) this;
        return null;
    }

    /**
     * Check if this elements type is of elementClass.
     *
     * @param elementClass The class to check this DataElement for.
     * @return True if the elementClass matches the DataElement class.
     */
    public boolean isOf(Class<? extends DataElement> elementClass) {
        return cachedType == elementClass;
    }

    /**
     * Get this element as a DataPrimitive, or throw.
     *
     * @return This element as a DataPrimitive.
     * @throws UnsupportedOperationException If this element is not a DataPrimitive.
     */
    public DataPrimitive asPrimitive() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a primitive!");
    }

    private String[] calcPath() {
        final int parents = parents();
        return calcPath(new String[parents + 1], parents);
    }

    private String[] calcPath(String[] collected, int index) {
        collected[index] = name;
        if (index == 0) return collected;
        return parent.calcPath(collected, --index);
    }

    /**
     * Cast this element to E
     *
     * @param <E>          The type to cast this element to.
     * @param elementClass The class to cast this element with.
     * @return The same DataElement as E, or null if this element does not conform to elementClass.
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E castOrNull(Class<E> elementClass) {
        return elementClass.isInstance(this) ? (E) this : null;
    }

    /**
     * Check if this DataElement has a name.
     *
     * @return True if this elements name is not null
     */
    public boolean hasName() {
        return name != null;
    }

    /**
     * Check if this DataElement has a parent
     *
     * @return True if this elements parent is not null.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Run the arrayConsumer if this element is a DataArray.
     *
     * @param arrayConsumer The if action.
     */
    public void ifArray(Consumer<DataArray> arrayConsumer) {
        ifArray(arrayConsumer, null);
    }

    /**
     * Run the arrayConsumer if this element is a DataArray, or the elseAction.
     *
     * @param arrayConsumer The if action.
     * @param elseAction    The else action.
     */
    public void ifArray(Consumer<DataArray> arrayConsumer, Runnable elseAction) {
        if (elseAction != null) elseAction.run();
    }

    /**
     * Run the listConsumer if this element is a DataList.
     *
     * @param listConsumer The if action.
     */
    public final void ifList(Consumer<DataList> listConsumer) {
        ifList(listConsumer, null);
    }

    /**
     * Run the listConsumer if this element is a DataList, or the elseAction.
     *
     * @param listConsumer The if action.
     * @param elseAction   The else action.
     */
    public void ifList(Consumer<DataList> listConsumer, Runnable elseAction) {
        if (elseAction != null) elseAction.run();
    }

    /**
     * Run the mapConsumer if this element is a DataMap.
     *
     * @param mapConsumer The if action.
     */
    public final void ifMap(Consumer<DataMap> mapConsumer) {
        ifMap(mapConsumer, null);
    }

    /**
     * Run the mapConsumer if this element is a DataMap, or the elseAction.
     *
     * @param mapConsumer The if action.
     * @param elseAction  The else action.
     */
    public void ifMap(Consumer<DataMap> mapConsumer, Runnable elseAction) {
        if (elseAction != null) elseAction.run();
    }

    /**
     * Run the primitiveConsumer if this element is a DataPrimitive.
     *
     * @param primitiveConsumer The if action.
     */
    public final void ifPrimitive(Consumer<DataPrimitive> primitiveConsumer) {
        ifPrimitive(primitiveConsumer, null);
    }

    /**
     * Run the primitiveConsumer if this element is a DataPrimitive, or the elseAction.
     *
     * @param primitiveConsumer The if action.
     * @param elseAction        The else action.
     */
    public void ifPrimitive(Consumer<DataPrimitive> primitiveConsumer, Runnable elseAction) {
        if (elseAction != null) elseAction.run();
    }

    /**
     * Check if this element is a DataArray.
     *
     * @return True if this element is a DataArray.
     */
    public boolean isArray() {
        return false;
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
     * Check if the data (parent / name) is initialized.
     * Initializing the data can be done through a constructor or {@link DataElement#setData(DataElement, String)}
     *
     * @return True if the parent or name is set.
     */
    public boolean isDataSet() {
        return dataSet;
    }

    /**
     * Check if this element is a DataList.
     *
     * @return True if this element is a DataList.
     */
    public boolean isList() {
        return false;
    }

    /**
     * Check if this element is a DataMap.
     *
     * @return True if this element is a DataMap.
     */
    public boolean isMap() {
        return false;
    }

    /**
     * Check if this element is a DataNull.
     *
     * @return True if this element is a DataNull.
     */
    public boolean isNull() {
        return false;
    }

    /**
     * Check if this element is not empty.
     *
     * @return True if this element is not empty.
     */
    public boolean isPresent() {
        return !isEmpty();
    }

    /**
     * Check if this element is empty.
     *
     * @return True if this element is empty. Implementation may differ.
     */
    public abstract boolean isEmpty();

    /**
     * Check if this element is a DataPrimitive.
     *
     * @return True if this element is a DataPrimitive.
     */
    public boolean isPrimitive() {
        return false;
    }

    /**
     * Get the name of this element.
     *
     * @return The name of this element.
     */
    public String name() {
        return name;
    }

    /**
     * Set the name of this element.
     *
     * @param name The name to set.
     * @return The same DataElement.
     * @throws IllegalArgumentException When the data is already set.
     */
    public DataElement name(String name) throws IllegalArgumentException {
        return setData(null, name);
    }

    /**
     * Get the parent of this element.
     *
     * @return The parent of this element.
     */
    public DataElement parent() {
        return parent;
    }

    /**
     * Get the amount of parents of this element.
     *
     * @return The amount of parents this element has, or in other words, how deeply nested this element is.
     * Returns 0 if this element has no parent.
     */
    protected int parents() {
        if (!hasParent()) return 0;
        return parent.parents() + 1;
    }

    /**
     * Get the path of the element, to the root of the structure.
     *
     * @return The path of this element as an array of Strings, where the first String is the name of the root element, and the last String the name of this element.
     */
    public String[] path() {
        if (cachedPath == null) cachedPath = calcPath();
        return cachedPath;
    }

    /**
     * Get this element as T, or throw.
     *
     * @param elementClass The class this element is required to be.
     * @param <T>          The type of {@code elementClass}.
     * @return This element, cast to T.
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
     * Unsafe cast this element to E
     *
     * @param <E> The type to cast this element to.
     * @return The cast element.
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E unsafeCast() {
        return (E) this;
    }

    @Override
    public int compareTo(DataElement o) {
        return name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        Object raw = raw();
        result = 31 * result + (raw != null ? raw.hashCode() : 0);
        return result;
    }

    /**
     * Get the raw value that backs this element.
     *
     * @return The raw value that backs this element.
     */
    protected abstract Object raw();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataElement that)) return false;
        return Objects.equals(name, that.name) && Objects.equals(raw(), that.raw());
    }

    /**
     * Clone this element.
     *
     * @return A new DataElement of the original type, <b>without it's data set.</b>
     */
    public abstract DataElement clone();
}
