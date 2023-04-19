package io.github.thegatesdev.maple.data;

import java.util.Arrays;
import java.util.Iterator;
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
 * An array element backed by a Java array.
 */
public class DataArray extends DataElement implements IndexedElement {

    private final DataElement[] value;

    DataArray(DataElement[] value) {
        this.value = value;
    }

    /**
     * Constructs a DataArray with its data unset.
     *
     * @param size The size to initialize the array with.
     */
    public DataArray(int size) {
        this(new DataElement[size]);
    }

    /**
     * Constructs a DataArray with its parent defaulted to {@code null}
     *
     * @param name The name to initialize the data with.
     * @param size The size to initialize the array with.
     */
    public DataArray(final String name, int size) {
        this(size);
        setData(null, name);
    }


    /**
     * Read an array to a DataArray
     *
     * @param data The array to read from.
     * @return A new DataArray with the same size as the array,
     * the values read using {@link DataElement#readOf(Object)}
     */
    public static DataArray read(Object[] data) {
        final DataArray output = new DataArray(data.length);
        for (int i = 0; i < data.length; i++) {
            output.set(i, DataElement.readOf(data[i]));
        }
        return output;
    }

    /**
     * Set an element at the specified index.
     *
     * @param index   The index to put the element at.
     * @param element The element to put.
     * @return This same DataArray.
     */
    public DataArray set(int index, DataElement element) {
        if (element.isDataSet())
            throw new IllegalArgumentException("This element already has a parent / name. Did you mean to copy() first?");
        if (value.length > index) value[index] = element.setData(this, "[" + index + "]");
        return this;
    }

    /**
     * Get the element at this index, or null.
     *
     * @param index The index of the element.
     * @return The element at this index, or {@code null}.
     */
    @Override
    public DataElement getOrNull(int index) {
        if (value.length <= index) return null;
        return value[index];
    }

    @Override
    public DataElement get(int index) {
        final DataElement element = getOrNull(index);
        if (element == null) return new DataNull().setData(this, "[" + index + "]");
        return element;
    }

    @Override
    public DataElement[] value() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public DataArray asArray() {
        return this;
    }

    @Override
    public void ifArray(final Consumer<DataArray> arrayConsumer, final Runnable elseAction) {
        arrayConsumer.accept(this);
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return value.length == 0;
    }

    @Override
    protected DataElement[] raw() {
        return value;
    }

    @Override
    public DataArray clone() {
        final DataArray cloned = new DataArray(value.length);
        for (int i = 0, valueLength = value.length; i < valueLength; i++) {
            cloned.set(i, this.value[i].clone());
        }
        return cloned;
    }

    @Override
    public DataArray name(String name) throws IllegalArgumentException {
        super.name(name);
        return this;
    }

    /**
     * @return The length of this array.
     */
    public int size(){
        return value.length;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DataArray that)) return false;
        return Objects.equals(name(), that.name()) && Arrays.equals(raw(), that.raw());
    }

    @Override
    public Iterator<DataElement> iterator() {
        return Arrays.stream(value).iterator();
    }
}
