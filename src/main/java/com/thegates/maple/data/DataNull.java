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
    public DataNull copy(DataElement parent, String name) {
        return new DataNull(parent, name);
    }

    @Override
    public Object getValue() {
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
    public boolean isOf(Class<? extends DataElement> elementClass) {
        return elementClass == DataNull.class;
    }
}
