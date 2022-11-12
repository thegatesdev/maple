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

    private List<DataElement> value;

    public DataList() {
    }

    public DataList(String name) {
        super(name);
    }

    protected DataList(DataElement parent, String name) {
        super(parent, name);
    }

    public static DataList read(List<?> list) {
        return read(list.toArray());
    }

    public static DataList read(Object... objects) {
        final DataList dataList = new DataList();
        synchronized (MODIFY_MUTEX) {
            for (Object o : objects) {
                dataList.add(DataElement.readOf(o));
            }
        }
        return dataList;
    }

    public DataList addAll(DataList dataList) {
        return addAll(dataList.value);
    }

    public DataList addAll(List<DataElement> elements) {
        if (elements != null && !elements.isEmpty()) {
            if (value == null) init(elements);
            else {
                synchronized (MODIFY_MUTEX) {
                    this.value.addAll(elements);
                }
            }
        }
        return this;
    }

    public List<DataElement> getValue() {
        if (value == null) return Collections.emptyList();
        return Collections.unmodifiableList(value);
    }

    @Override
    protected Object value() {
        return value;
    }

    private void init(Collection<DataElement> input) {
        if (value == null)
            if (input == null)
                value = new LinkedList<>();
            else
                value = new LinkedList<>(input);
    }

    public DataList add(DataElement element) {
        if (value == null) init(null);
        if (element.hasDataSet()) throw new IllegalArgumentException("This element already has a parent / name. Did you mean to copy() first?");
        value.add(element.setData(this, "[" + value.size() + "]"));
        return this;
    }

    @Override
    public Iterator<DataElement> iterator() {
        return value.iterator();
    }

    @Override
    public Spliterator<DataElement> spliterator() {
        return value.spliterator();
    }

    public <T> List<T> listOf(Class<T> elementClass) {
        final LinkedList<T> out = new LinkedList<>();
        new ElementIterator<>(DataPrimitive.class).forEachRemaining(primitive -> {
            if (primitive.isValueOf(elementClass)) out.add(primitive.getValueUnsafe());
        });
        return out;
    }


    public int size() {
        if (value == null) return 0;
        return value.size();
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    public boolean isPresent() {
        return value != null && !value.isEmpty();
    }


    // --


    @Override
    public DataElement clone() {
        return new DataList().addAll(this);
    }

    @Override
    public boolean isDataPrimitive() {
        return false;
    }

    @Override
    public boolean isDataList() {
        return true;
    }

    @Override
    public boolean isDataMap() {
        return false;
    }

    @Override
    public boolean isDataNull() {
        return false;
    }

    @Override
    public DataList getAsDataList() {
        return this;
    }


    // --


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataList)) return false;
        return super.equals(o);
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

    public class ElementIterator<E extends DataElement> implements Iterator<E> {
        private final Class<E> elementClass;
        private final Iterator<DataElement> iterator;
        private E next;

        public ElementIterator(Class<E> elementClass) {
            this.elementClass = elementClass;
            iterator = value.iterator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasNext() {
            if (!iterator.hasNext()) return false;
            final DataElement el = iterator.next();
            if (el.isOf(elementClass)) {
                next = (E) el;
                return true;
            }
            return false;
        }

        @Override
        public E next() {
            return next;
        }
    }
}
