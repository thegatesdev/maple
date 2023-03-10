package io.github.thegatesdev.maple.data;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;

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
 * A list element backed by an ArrayList, containing an array of DataElement.
 * It allows for more advanced iteration, for example by element type ({@link DataList#iterator(Class)}.
 */
public class DataList extends IndexedElement {

    private final IntFunction<List<DataElement>> listSupplier;
    private List<DataElement> value;

    /**
     * Constructs an empty DataList with its data unset.
     */
    public DataList() {
        this(null, null);
    }

    /**
     * Constructs an empty DataList with its parent defaulted to {@code null}.
     *
     * @param name The name to initialize the data with.
     */
    public DataList(String name) {
        this(name, null);
    }

    /**
     * Constructs an empty DataList with its parent defaulted to {@code null}.
     *
     * @param name The name to initialize the data with.
     * @param listSupplier  An IntFunction to supply a list when initializing, taking an initial capacity.
     */
    public DataList(String name, IntFunction<List<DataElement>> listSupplier){
        this.listSupplier = listSupplier;
        if (name != null) setData(null, name);
    }

    /**
     * Constructs an empty DataList with its data unset.
     *
     * @param listSupplier  An IntFunction to supply a list when initializing, taking an initial capacity.
     */
    public DataList(IntFunction<List<DataElement>> listSupplier){
        this(null, listSupplier);
    }


    /**
     * Read an Object iterable to a DataList.
     *
     * @param objects The iterable to read from.
     * @return A new DataList containing all the elements returned from the iterable,
     * read using {@link DataElement#readOf(Object)}
     */
    public static DataList read(Iterable<?> objects) {
        final DataList dataList = new DataList();
        for (Object o : objects) {
            dataList.add(DataElement.readOf(o));
        }
        return dataList;
    }

    /**
     * Read an Object varargs array to a DataList.
     *
     * @param objects The array to read from.
     * @return A new DataList containing all the elements returned from the array,
     * read using {@link DataElement#readOf(Object)}
     */
    public static DataList read(Object... objects) {
        final DataList dataList = new DataList();
        for (Object o : objects) {
            dataList.add(DataElement.readOf(o));
        }
        return dataList;
    }


    /**
     * @param element The element to add to this DataList.
     * @return This DataList.
     * @throws IllegalArgumentException When the supplied DataElement already has its data set.
     */
    public DataList add(DataElement element) throws IllegalArgumentException {
        if (element.isDataSet())
            throw new IllegalArgumentException("This element already has a parent / name. Did you mean to copy() first?");
        if (value == null) init(1);
        synchronized (MODIFY_MUTEX) {
            value.add(element.setData(this, "[" + value.size() + "]"));
        }
        return this;
    }

    /**
     * Get the element at the specified position in this list.
     *
     * @param index Index of the element to return.
     * @return The element at the specified position in this list, or null if out of bounds.
     */
    @Override
    public DataElement getOrNull(int index) {
        try {
            return value.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * @return The size of this list, or {@code 0} if the list is not initialized.
     */
    public int size() {
        if (value == null) return 0;
        return value.size();
    }

    /**
     * Sort this list using the default sort method.
     *
     * @see DataElement#compareTo(DataElement)
     */
    public void sort() {
        value.sort(DataElement::compareTo);
    }

    /**
     * See {@link ArrayList#sort(Comparator)}
     *
     * @param comparator the Comparator used to compare list elements. A null value indicates that the elements' natural ordering should be used
     */
    public void sort(Comparator<? super DataElement> comparator) {
        value.sort(comparator);
    }

    /**
     * @param dataList The DataList to clone the elements from.
     * @return This DataList.
     * @see DataList#cloneFrom(Collection)
     */
    public DataList cloneFrom(DataList dataList) {
        return cloneFrom(dataList.value);
    }

    /**
     * @param elements The Collection to clone the elements from.
     * @return This DataList.
     */
    public DataList cloneFrom(Collection<DataElement> elements) {
        if (elements != null && !elements.isEmpty()) {
            if (value == null) init(elements.size());
            for (final DataElement element : elements) add(element.clone());
        }
        return this;
    }

    /**
     * @param elementClass The class to get the iterator for.
     * @param <E>          The type to get the iterator for.
     * @return The iterator for elements of this {@code elementClass}.
     */
    public <E extends DataElement> Iterator<E> iterator(Class<E> elementClass) {
        return new ClassedIterator<>(elementClass, iterator());
    }

    /**
     * @param elementClass The class the DataPrimitive's values should be of.
     * @param <T>          The type the DataPrimitive's values should be of.
     * @return A new ArrayList containing the values of the DataPrimitives in this DataList conforming to {@code elementClass}.
     */
    public <T> ArrayList<T> primitiveList(Class<T> elementClass) {
        final ArrayList<T> out = new ArrayList<>();
        synchronized (READ_MUTEX) {
            for (final DataElement element : this) {
                if (element.isPrimitive() && element.asPrimitive().valueOf(elementClass))
                    out.add(element.asPrimitive().valueUnsafe());
            }
        }
        return out;
    }

    private void init(int initialCapacity) {
        if (value == null) {
            if (listSupplier == null) value = new ArrayList<>(initialCapacity);
            else{
                final List<DataElement> suppliedList = listSupplier.apply(initialCapacity);
                if (!suppliedList.isEmpty()) throw new IllegalArgumentException("List supplier should return empty list");
                this.value = suppliedList;
            }
        }
    }

    @Override
    public Iterator<DataElement> iterator() {
        return value.iterator();
    }

    @Override
    public Spliterator<DataElement> spliterator() {
        return value.spliterator();
    }

    @Override
    public String toString() {
        if (value == null || value.isEmpty()) return "emptyList";
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dataList[");
        int len = value.size();
        for (DataElement element : value) {
            stringBuilder.append(element.toString());
            if (--len > 0) stringBuilder.append(", ");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public List<DataElement> value() {
        if (value == null) return Collections.emptyList();
        return Collections.unmodifiableList(value);
    }

    @Override
    public DataList asList() {
        return this;
    }

    @Override
    public void ifList(final Consumer<DataList> listConsumer, final Runnable elseAction) {
        listConsumer.accept(this);
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public DataList name(String name) throws IllegalArgumentException {
        super.name(name);
        return this;
    }

    /**
     * Check if this list is empty, or not initialized.
     */
    @Override
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    protected List<DataElement> raw() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataList)) return false;
        return super.equals(o);
    }

    @Override
    public DataList clone() {
        return new DataList().cloneFrom(this);
    }

    private static class ClassedIterator<E extends DataElement> implements Iterator<E> {
        private final Class<E> elementClass;
        private final Iterator<DataElement> iterator;
        private E next;

        public ClassedIterator(Class<E> elementClass, Iterator<DataElement> iterator) {
            this.elementClass = elementClass;
            this.iterator = iterator;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasNext() {
            while (iterator.hasNext()) {
                final DataElement el = iterator.next();
                if (el.isOf(elementClass)) {
                    next = (E) el;
                    return true;
                }
            }
            return false;
        }

        @Override
        public E next() {
            return next;
        }
    }
}
