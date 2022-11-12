package com.thegates.maple.data;

public class DataNull extends DataElement {


    public DataNull() {
    }

    public DataNull(String name) {
        super(name);
    }

    protected DataNull(DataElement parent, String name) {
        super(parent, name);
    }

    @Override
    public DataNull copy() {
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
}
