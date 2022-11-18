package com.thegates.maple.data;

import java.util.*;

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

public class DataList extends DataElement implements Iterable<DataElement>, Cloneable, Comparable<DataElement> {

    private ArrayList<DataElement> value;

    public DataList() {
    }

    public DataList(String name) {
        super(name);
    }


    public static DataList read(Collection<?> list) {
        return read(list.toArray());
    }

    public static DataList read(Object... objects) {
        final DataList dataList = new DataList();
        for (Object o : objects) {
            dataList.add(DataElement.readOf(o));
        }
        return dataList;
    }

    public DataList add(DataElement element) {
        if (element.isDataSet())
            throw new IllegalArgumentException("This element already has a parent / name. Did you mean to copy() first?");
        if (value == null) init(1);
        synchronized (MODIFY_MUTEX) {
            value.add(element.setData(this, "[" + value.size() + "]"));
        }
        return this;
    }

    private void init(int initialCapacity) {
        if (value == null)
            value = new ArrayList<>(initialCapacity);
    }

    public <E extends DataElement> Iterator<E> iterator(Class<E> elementClass) {
        return new ClassedIterator<>(elementClass);
    }

    public <T> ArrayList<T> primitiveList(Class<T> elementClass) {
        final ArrayList<T> out = new ArrayList<>();
        synchronized (READ_MUTEX) {
            for (final DataElement element : this) {
                if (element.isPrimitive() && element.asPrimitive().isValueOf(elementClass))
                    out.add(element.asPrimitive().valueUnsafe());
            }
        }
        return out;
    }

    public int size() {
        if (value == null) return 0;
        return value.size();
    }

    public void sort() {
        value.sort(DataElement::compareTo);
    }

    public void sort(Comparator<? super DataElement> comparator) {
        value.sort(comparator);
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

    public List<DataElement> value() {
        if (value == null) return Collections.emptyList();
        return Collections.unmodifiableList(value);
    }

    @Override
    public DataList asList() {
        return this;
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public boolean isPrimitive() {
        return false;
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

    @Override
    protected ArrayList<DataElement> raw() {
        return value;
    }

    public DataList cloneFrom(DataList dataList) {
        return cloneFrom(dataList.value);
    }

    public DataList cloneFrom(List<DataElement> elements) {
        if (elements != null && !elements.isEmpty()) {
            if (value == null) init(elements.size());
            for (final DataElement element : elements) add(element.clone());
        }
        return this;
    }

    private class ClassedIterator<E extends DataElement> implements Iterator<E> {
        private final Class<E> elementClass;
        private final Iterator<DataElement> iterator;
        private E next;

        public ClassedIterator(Class<E> elementClass) {
            this.elementClass = elementClass;
            iterator = iterator();
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
