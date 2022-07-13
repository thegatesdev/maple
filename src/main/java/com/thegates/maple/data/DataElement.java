package com.thegates.maple.data;

import java.util.Objects;

public abstract class DataElement {

    protected DataElement parent;
    protected String name;
    protected String path;

    DataElement() {
    }

    public String getName() {
        return name;
    }

    public DataElement setName(String name) {
        this.name = name;
        updatePath();
        return this;
    }

    public DataElement setParent(DataElement parent) {
        this.parent = parent;
        updatePath();
        return this;
    }

    private void updatePath() {
        if (parent == null) this.path = name;
        else this.path = parent.path + "." + name;
    }

    public String getDescription() {
        return "DataElement at " + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataElement that)) return false;
        return Objects.equals(parent, that.parent) && Objects.equals(name, that.name) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, name, path);
    }
}
