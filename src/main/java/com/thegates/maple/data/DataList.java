package com.thegates.maple.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class DataList extends DataElement {

    private List<DataContainer> values;

    public DataList() {
    }

    public DataList(int initialCapacity) {
        init(initialCapacity);
    }


    public static synchronized DataList read(List<?> list) {
        DataList dataList = new DataList(list.size());
        list.stream().map(DataContainer::read).forEach(dataList::add);
        return dataList;
    }

    public static synchronized DataList of(Object... objects) {
        DataList dataList = new DataList(objects.length);
        for (Object o : objects) {
            dataList.add(new DataContainer(o));
        }
        return dataList;
    }

    public DataList addAllFrom(DataList dataList) {
        dataList.values.forEach(this::add);
        return this;
    }

    private void init(int initialCapacity) {
        if (values == null)
            values = new ArrayList<>(initialCapacity);
    }

    @Override
    public DataList setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public DataElement setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    public <T> List<T> getAsListOf(Class<T> clazz) {
        if (values == null || values.isEmpty()) return Collections.emptyList();
        final List<T> output = new ArrayList<>(values.size());
        synchronized (this) {
            values.forEach(value -> {
                if (value.isOf(clazz)) output.add(value.getOrThrow(clazz));
            });
        }
        return Collections.unmodifiableList(output);
    }

    public <T> List<T> getValuesUnsafe() {
        if (values == null || values.isEmpty()) return Collections.emptyList();
        final List<T> output = new ArrayList<>(values.size());
        synchronized (this) {
            values.forEach(value -> output.add(value.getUnsafe()));
        }
        return output;
    }

    public Stream<DataContainer> stream(Class<?> clazz) {
        if (values == null || values.isEmpty()) return Stream.empty();
        return values.stream().filter(dataContainer -> dataContainer.isOf(clazz));
    }

    public Stream<DataContainer> stream() {
        if (values == null) return Stream.empty();
        return values.stream();
    }

    void add(DataContainer container) {
        if (values == null) init(1);
        values.add(container.copy().setParent(this).setName("[" + values.size() + "]"));
    }

    public void add(Object o) {
        add(new DataContainer(o));
    }

    public List<DataContainer> getValues() {
        if (values == null) return Collections.emptyList();
        return Collections.unmodifiableList(values);
    }


    public int size() {
        if (values == null) return 0;
        return values.size();
    }

    public boolean isEmpty() {
        if (values == null) return true;
        return values.isEmpty();
    }


    @Override
    public DataElement copy() {
        return new DataList().addAllFrom(this);
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
