package com.thegates.maple.data;

public class DataNull extends DataElement {
    public DataNull() {
    }

    public DataNull(DataElement parent, String name) {
        super(parent, name);
    }

    @Override
    public DataNull copy() {
        return new DataNull();
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
