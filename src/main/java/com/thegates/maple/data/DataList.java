package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DataList extends DataElement {

    private final List<DataContainer> values;

    public DataList() {
        this(0);
    }

    public DataList(int initialCapacity) {
        values = new ArrayList<>(initialCapacity);
    }

    public static synchronized DataList of(List<?> list) {
        DataList dataList = new DataList(list.size());
        list.forEach(o -> dataList.add(DataContainer.of(o).setParent(dataList)));
        return dataList;
    }

    public static synchronized DataList of(Object... objects) {
        DataList dataList = new DataList(objects.length);
        for (Object o : objects) {
            dataList.add(DataContainer.of(o).setParent(dataList));
        }
        return dataList;
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

    public <T> List<T> getAsListOf(Class<T> clazz) {
        final List<T> output = new ArrayList<>(values.size());
        synchronized (this) {
            values.forEach(value -> {
                if (value.isOf(clazz)) output.add(value.getOrThrow(clazz));
            });
        }
        return Collections.unmodifiableList(output);
    }

    public <T> List<T> getValuesUnsafe() {
        final List<T> output = new ArrayList<>(values.size());
        synchronized (this) {
            values.forEach(value -> output.add(value.getUnsafe()));
        }
        return output;
    }

    public Stream<DataContainer> stream(Class<?> clazz) {
        return values.stream().filter(dataContainer -> dataContainer.isOf(clazz));
    }

    public Stream<DataContainer> stream() {
        return values.stream();
    }

    @Override
    public String getDescription() {
        return String.format(super.getDescription() + ": DataList size %s", values.size());
    }

    public void add(DataContainer element) {
        values.add(element.setParent(this).setName("[" + values.size() + "]"));
    }

    public void add(Object o) {
        add(DataContainer.of(o));
    }

    public List<DataContainer> getValues() {
        return Collections.unmodifiableList(values);
    }


    public DataList requireValuesOf(Class<?> clazz) {
        if (!values.isEmpty()) {
            values.forEach(value -> {
                if (!value.isOf(clazz))
                    throw new ReadException(this, "list requires items of type " + clazz.getSimpleName());
            });
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
