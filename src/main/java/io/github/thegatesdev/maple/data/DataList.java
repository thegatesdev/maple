package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.Maple;

import java.util.Collections;
import java.util.List;
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
 * An element holding multiple values in a list.
 */
public class DataList extends DataElement implements MappedElements<Integer> {

    private final List<DataElement> elements, view;

    DataList(List<DataElement> elements) {
        this.elements = elements;
        this.view = Collections.unmodifiableList(elements);
    }

    // -- BASIC OPERATIONS

    /**
     * Replaces the element at the specified index with the specified element.
     * Also see {@link List#set(int, Object)}.
     *
     * @param index   The index of the element to replace.
     * @param element The element to be put at the index.
     * @return The previous element mapped at this index.
     */
    public DataElement set(int index, DataElement element) {
        requireNotLocked();
        var old = elements.set(index, element);
        connectThis(element, index);
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Adds the specified element to the end of the list.
     * Also see {@link List#add(Object)}.
     *
     * @param element The element to add.
     */
    public void add(DataElement element) {
        requireNotLocked();
        elements.add(element);
        connectThis(element, size());
    }

    /**
     * Removed the element at this index, shifting any elements after that to the left.
     * Also see {@link List#remove(int)}.
     *
     * @param index The index of the element to remove.
     * @return The removed element.
     */
    public DataElement remove(int index) {
        requireNotLocked();
        var old = elements.remove(index);
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Get the element at the specified index, or null.
     *
     * @param index The index of the element to get.
     * @return The element at the specified index, or {@code null}.
     */
    public DataElement getOrNull(int index) {
        if (index < 0 || index >= elements.size()) return null;
        return elements.get(index);
    }

    /**
     * Get the element at the specified index.
     *
     * @param index The index of the element to get.
     * @return The element at the specified index, or a new {@link DataNull}.
     */
    public DataElement get(int index) {
        var el = getOrNull(index);
        return el == null ? connectThis(new DataNull(), index) : el;
    }

    /**
     * Get the element at the specified index, or null.
     *
     * @param index The index of the element to get.
     * @return The element at the specified index, or {@code null}.
     */
    @Override
    public DataElement getOrNull(Integer index) {
        return getOrNull((int) index);
    }

    /**
     * Get the element at the specified index.
     *
     * @param index The index of the element to get.
     * @return The element at the specified index, or a new {@link DataNull}.
     */
    @Override
    public DataElement get(Integer index) {
        return get((int) index);
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        return elements.size();
    }

    // -- ELEMENT

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public DataList asList() throws UnsupportedOperationException {
        return this;
    }

    @Override
    public void ifList(Consumer<DataList> listConsumer, Runnable elseAction) {
        listConsumer.accept(this);
    }

    @Override
    public DataList shallowCopy() {
        return new DataList(elements);
    }

    @Override
    public DataList deepCopy() {
        var copy = new DataList(Maple.DEFAULT_LIST_IMPL.apply(elements.size()));
        for (int i = 0; i < elements.size(); i++) copy.set(i, get(i).deepCopy());
        return copy;
    }

    private DataElement connectThis(DataElement element, int index) {
        return element.connect(this, "[" + index + "]");
    }

    @Override
    protected Object raw() {
        return elements;
    }

    @Override
    public Object view() {
        return view;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public String toString() {
        if (elements.isEmpty()) return "emptyList";
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dataList[");
        for (int i = 0, elementsSize = elements.size(); i < elementsSize; i++) {
            stringBuilder.append(elements.get(i).toString());
            if (i == elementsSize - 1) stringBuilder.append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
