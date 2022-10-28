package com.thegates.maple.data;

import com.thegates.maple.exception.RequireTypeException;

import java.util.List;
import java.util.Map;
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

public abstract class DataElement {

    static final Object MODIFY_MUTEX = new Object();
    static final Object GET_MUTEX = new Object();

    protected DataElement parent;
    protected String name;

    DataElement() {
    }

    DataElement(DataElement parent, String name) {
        this.parent = parent;
        this.name = name == null ? "root" : name;
    }


    public static DataElement readOf(Object o) {
        if (o == null) return new DataNull();
        if (o instanceof Map<?, ?> map) return DataMap.readInternal(map);
        if (o instanceof List<?> list) return DataList.read(list);
        return new DataPrimitive(o);
    }

    public DataElement setParent(DataElement parent) {
        this.parent = parent;
        return this;
    }

    public DataElement setName(String name) {
        this.name = name;
        return this;
    }

    public abstract DataElement copy();

    public abstract boolean isDataPrimitive();

    public abstract boolean isDataList();

    public abstract boolean isDataMap();

    public abstract boolean isDataNull();


    @SuppressWarnings("unchecked")
    public <T extends DataElement> T requireOf(Class<T> elementClass) throws RequireTypeException {
        if (!elementClass.isInstance(this)) throw new RequireTypeException(this, elementClass);
        return ((T) this);
    }


    public DataPrimitive getAsDataPrimitive() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a primitive!");
    }

    public DataList getAsDataList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a list!");
    }

    public DataMap getAsDataMap() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a map!");
    }


    public String getPath() {
        final String n = name == null ? "" : name;
        return parent == null ? n : parent.getPath() + "." + n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataElement that)) return false;
        return Objects.equals(parent, that.parent) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = parent != null ? parent.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
