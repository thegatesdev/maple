package com.thegates.maple.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

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

public class DataList extends DataElement {

    private List<DataContainer> values;

    public DataList() {
    }

    public DataList(int initialCapacity) {
        init(initialCapacity);
    }


    public static DataList read(List<?> list) {
        DataList dataList = new DataList(list.size());
        synchronized (MODIFY_MUTEX) {
            list.forEach(o -> dataList.add(DataContainer.read(o)));
        }
        return dataList;
    }

    public static DataList read(Object... objects) {
        DataList dataList = new DataList(objects.length);
        synchronized (MODIFY_MUTEX) {
            for (Object o : objects) {
                dataList.add(DataContainer.read(o));
            }
        }
        return dataList;
    }

    public DataList addAllFrom(DataList dataList) {
        synchronized (MODIFY_MUTEX) {
            dataList.values.forEach(this::add);
        }
        return this;
    }

    private void init(int initialCapacity) {
        if (values == null)
            values = new ArrayList<>(initialCapacity);
    }

    @Override
    public DataList setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public DataElement setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    public <T> List<T> getAsListOf(Class<T> clazz) {
        if (values == null || values.isEmpty()) return Collections.emptyList();
        final List<T> output = new ArrayList<>(values.size());
        synchronized (GET_MUTEX) {
            values.forEach(value -> {
                if (value.isValueOf(clazz)) output.add(value.getValueOrThrow(clazz));
            });
        }
        return Collections.unmodifiableList(output);
    }

    public <T> List<T> getValuesUnsafe() {
        if (values == null || values.isEmpty()) return Collections.emptyList();
        final List<T> output = new ArrayList<>(values.size());
        synchronized (GET_MUTEX) {
            values.forEach(value -> output.add(value.getValueUnsafe()));
        }
        return output;
    }

    public Stream<DataContainer> stream(Class<?> clazz) {
        if (values == null || values.isEmpty()) return Stream.empty();
        synchronized (GET_MUTEX) {
            return values.stream().filter(dataContainer -> dataContainer.isValueOf(clazz));
        }
    }

    public Stream<DataContainer> stream() {
        if (values == null) return Stream.empty();
        return values.stream();
    }

    void add(DataContainer container) {
        if (values == null) init(1);
        values.add(container.copy().setParent(this).setName("[" + values.size() + "]"));
    }

    public List<DataContainer> getValues() {
        if (values == null) return Collections.emptyList();
        return Collections.unmodifiableList(values);
    }


    public int size() {
        if (values == null) return 0;
        return values.size();
    }

    public boolean isEmpty() {
        if (values == null) return true;
        return values.isEmpty();
    }


    @Override
    public DataElement copy() {
        return new DataList().addAllFrom(this);
    }

    @Override
    public boolean isDataContainer() {
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
    public DataContainer getAsDataContainer() {
        return null;
    }

    @Override
    public DataList getAsDataList() {
        return this;
    }

    @Override
    public DataMap getAsDataMap() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataList dataList)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(values, dataList.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), values);
    }

    @Override
    public String toString() {
        return values == null ? "emptyList" : String.join("\n", values.stream().map(DataContainer::toString).toList());
    }
}
