package com.thegates.maple.data;

import java.util.Objects;

public abstract class DataElement {

    protected DataElement parent;
    protected String name;

    DataElement() {
    }

    DataElement(DataElement parent, String name) {
        this.parent = parent;
        this.name = name == null ? "" : name;
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
        return Objects.hash(parent, name);
    }
}
