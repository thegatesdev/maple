package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.Maple;

import java.util.ArrayList;
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

    private final List<DataElement> elements;

    private DataList(List<DataElement> elements) {
        this.elements = elements;
    }

    public DataList() {
        this(new ArrayList<>());
    }

    public DataList(int initialCapacity) {
        this(new ArrayList<>(initialCapacity));
    }

    // -- OPERATIONS

    /**
     * Replace the element at the supplied index with the supplied element.
     *
     * @return The previous element at this index
     */
    public DataElement set(int index, DataElement element) {
        var old = elements.set(index, connectThis(element, index));
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Replace the element read from the supplied input at the supplied index with the supplied element.
     *
     * @return The previous element at this index
     */
    public DataElement set(int index, Object input) {
        var old = elements.set(index, connectThis(Maple.read(input), index));
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Add the supplied element to the end of the list.
     */
    public void add(DataElement element) {
        connectThis(element, size() + 1);
        elements.add(element);
    }

    /**
     * Add the element read from the supplied input to the end of the list.
     */
    public void add(Object input) {
        elements.add(connectThis(Maple.read(input), size() + 1));
    }

    /**
     * Remove the element at the supplied index, shifting any elements after that to the left.
     *
     * @return The element previously at the index
     */
    public DataElement remove(int index) {
        var old = elements.remove(index);
        if (old != null) ((DataElement) old).disconnect();
        return old;
    }

    private DataElement connectThis(DataElement element, int index) {
        return element.connect(this, "[" + index + "]");
    }

    /**
     * Clear the list of all containing elements.
     *
     * @return The amount of elements cleared, or the size before it was cleared
     */
    public int clear() {
        each(element -> ((DataElement) element).disconnect());
        int size = size();
        elements.clear();
        return size;
    }

    // -- GET

    /**
     * Get the element at the supplied index, or null if out of bounds.
     */
    public DataElement getOrNull(int index) {
        if (index < 0 || index >= elements.size()) return null;
        return elements.get(index);
    }

    @Override
    public DataElement getOrNull(Integer integer) {
        return getOrNull(integer.intValue());
    }

    /**
     * Get the element at the supplied index, or a new DataNull if out of bounds.
     */
    public DataElement get(int index) {
        var el = getOrNull(index);
        return el == null ? connectThis(new DataNull(), index) : el;
    }

    @Override
    public DataElement get(Integer integer) {
        return get(integer.intValue());
    }

    /**
     * Get the size of this list, or how many elements it contains
     */
    public int size() {
        return elements.size();
    }

    /**
     * Run the supplied consumer for every element in this list.
     */
    public void each(Consumer<DataElement> elementConsumer) {
        for (int i = 0; i < elements.size(); i++) elementConsumer.accept(elements.get(i));
    }

    // -- SELF

    @Override
    public List<DataElement> view() {
        return Collections.unmodifiableList(elements);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public DataList copy() {
        var copy = new DataList(elements.size());
        for (int i = 0; i < elements.size(); i++) copy.set(i, get(i).copy());
        return copy;
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

    /**
     * @return {@code true}
     */
    public boolean isList() {
        return true;
    }

    public DataList asList() {
        return this;
    }
}
