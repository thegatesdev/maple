package com.thegates.maple.data;

public class DataNull extends DataElement implements Cloneable, Comparable<DataElement> {


    public DataNull() {
    }

    protected DataNull(String name) {
        setData(null, name);
    }

    protected DataNull(DataElement parent, String name) {
        setData(parent, name);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public DataNull clone() {
        return new DataNull();
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    protected Object value() {
        return null;
    }

    @Override
    public boolean isDataPrimitive() {
        return false;
    }

    @Override
    public boolean isDataList() {
        return false;
    }

    @Override
    public boolean isDataMap() {
        return false;
    }

    @Override
    public boolean isDataNull() {
        return true;
    }

    @Override
    public String toString() {
        return "dataNull";
    }
}