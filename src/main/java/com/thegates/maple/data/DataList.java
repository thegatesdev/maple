package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DataList extends DataElement {

    private final List<DataContainer> values;

    public DataList() {
        this(0);
    }

    public DataList(int initialCapacity) {
        values = new ArrayList<>(initialCapacity);
    }

    public synchronized static DataList of(List<?> list) {
        DataList dataList = new DataList(list.size());
        for (Object o : list) {
            dataList.add(DataContainer.of(o).setParent(dataList));
        }
        return dataList;
    }

    public static DataList of(Object... data) {
        return of(List.of(data));
    }

    @Override
    public DataList setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public DataList setName(String name) {
        super.setName(name);
        return this;
    }

    public synchronized <T> List<T> getAsListOf(Class<T> clazz) {
        final List<T> output = new ArrayList<>(values.size());
        for (DataContainer value : values) {
            if (value.isOf(clazz)) output.add(value.getOrThrow(clazz));
        }
        return Collections.unmodifiableList(output);
    }

    public synchronized <T> List<T> getValuesUnsafe() {
        final List<T> output = new ArrayList<>(values.size());
        for (DataContainer value : values) {
            output.add(value.getUnsafe());
        }
        return output;
    }

    @Override
    public String getDescription() {
        return String.format(super.getDescription() + ": DataList size %s", values.size());
    }

    public void add(DataContainer element) {
        final int size = values.size();
        values.add(element.setParent(this).setName("[" + size + "]"));
    }

    public void add(Object o) {
        add(DataContainer.of(o));
    }

    public List<DataContainer> getValues() {
        return Collections.unmodifiableList(values);
    }


    public DataList requireValuesOf(Class<?> clazz) {
        if (!values.isEmpty()) {
            for (DataContainer value : values) {
                if (!value.isOf(clazz))
                    throw new ReadException(this, "list requires items of type " + clazz.getSimpleName());
            }
        }
        return this;
    }

    public DataList requireValueOf(Class<?> clazz) {
        if (!values.isEmpty()) {
            for (DataContainer value : values) {
                if (value.isOf(clazz)) return this;
            }
        }
        throw new ReadException(this, "list requires at least one item of type " + clazz.getSimpleName());
    }

    public DataList requireSize(int size) {
        if (values.size() != size) throw new ReadException(this, "list requires size " + size);
        return this;
    }

    public DataList requireSizeHigher(int size) {
        if (values.size() <= size) throw new ReadException(this, "list requires size higher than " + size);
        return this;
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
}
