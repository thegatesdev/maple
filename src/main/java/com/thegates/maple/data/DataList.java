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

public class DataList extends DataElement implements Iterable<DataElement> {

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
        if (!elements.isEmpty()) {
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

    private void init(Collection<DataElement> input) {
        if (value == null)
            if (input == null)
                value = new LinkedList<>();
            else
                value = new LinkedList<>(input);
    }

    public DataList add(DataElement element) {
        if (value == null) init(null);
        value.add(element.copy(this, "[" + value.size() + "]"));
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
    public DataElement copy(DataElement parent, String name) {
        return new DataList(parent, name).addAll(this);
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
    public boolean isOf(Class<? extends DataElement> elementClass) {
        return elementClass == DataList.class;
    }

    @Override
    public DataList getAsDataList() {
        return this;
    }


    // --


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataList dataList)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(value, dataList.value);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return value == null ? "emptyList" : "dataList with\n\t" + String.join("\n", value.stream().map(DataElement::toString).toList());
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
