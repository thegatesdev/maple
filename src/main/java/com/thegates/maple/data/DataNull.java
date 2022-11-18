package com.thegates.maple.data;

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

public class DataNull extends DataElement implements Cloneable, Comparable<DataElement> {
    public DataNull() {
    }

    public DataNull(String name) {
        super(name);
    }

    public DataNull(DataElement parent, String name) {
        setData(parent, name);
    }

    @Override
    public String toString() {
        return "dataNull";
    }

    @Override
    public Object value() {
        return null;
    }

    @Override
    public DataNull clone() {
        return new DataNull();
    }

    @Override
    protected Object raw() {
        return null;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }
}
