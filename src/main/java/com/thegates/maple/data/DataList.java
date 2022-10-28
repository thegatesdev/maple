package com.thegates.maple.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public class DataList extends DataElement implements Iterable<DataElement> {

    private List<DataElement> values;

    public DataList() {
    }

    public DataList(int initialCapacity) {
        init(initialCapacity);
    }


    public static DataList read(List<?> list) {
        final DataList dataList = new DataList(list.size());
        synchronized (MODIFY_MUTEX) {
            list.forEach(o -> dataList.add(DataElement.readOf(o)));
        }
        return dataList;
    }

    public static DataList read(Object... objects) {
        final DataList dataList = new DataList(objects.length);
        synchronized (MODIFY_MUTEX) {
            for (Object o : objects) {
                dataList.add(DataElement.readOf(o));
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

    void add(DataElement element) {
        if (values == null) init(1);
        values.add(element.setParent(this).setName("[" + values.size() + "]"));
    }

    @Override
    public Iterator<DataElement> iterator() {
        return values.iterator();
    }


    public int size() {
        if (values == null) return 0;
        return values.size();
    }

    public boolean isEmpty() {
        return values == null || values.isEmpty();
    }

    public boolean isPresent() {
        return values != null && !values.isEmpty();
    }


    // --

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

    @Override
    public DataElement copy() {
        return new DataList().addAllFrom(this);
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
        if (!(o instanceof DataList dataList)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(values, dataList.values);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (values != null ? values.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return values == null ? "emptyList" : "dataList with\n\t" + String.join("\n", values.stream().map(DataElement::toString).toList());
    }
}
