package com.thegates.maple.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class DataMap extends DataElement {

    private final Map<String, DataContainer> value;

    public DataMap() {
        this(1);
    }

    public DataMap(int initialCapacity) {
        value = new HashMap<>(initialCapacity);
    }

    public static DataMap of(Map<?, ?> data) {
        DataMap output = new DataMap(data.size());
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (entry.getKey() instanceof String key) {
                Object value = entry.getValue();
                DataContainer dataContainer = DataContainer.of(value);
                output.put(key, dataContainer);
            }
        }
        return output;
    }

    @Override
    public DataMap setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public DataMap setName(String name) {
        super.setName(name);
        return this;
    }

    public DataContainer get(String key) {
        if (key == null) {
            return new DataContainer().setParent(this).setName("none");
        }
        DataContainer container = getValue().get(key);
        return container != null ? container : new DataContainer().setParent(this).setName(key);
    }

    public void put(String key, DataContainer value) {
        getValue().put(key, value.setParent(this).setName(key));
    }

    public synchronized void putAll(DataMap dataMap) {
        dataMap.getValue().forEach(this::put);
    }

    public void doIfPresent(String key, Consumer<DataContainer> action) {
        if (has(key)) {
            action.accept(get(key));
        }
    }

    public <T> void doIfPresent(String key, Class<T> clazz, Consumer<T> action) {
        if (has(key)) {
            T val = get(key).getOrNull(clazz);
            if (val != null)
                action.accept(val);
        }
    }

    public boolean has(String key) {
        return getValue().containsKey(key);
    }

    public Map<String, DataContainer> getValue() {
        return Collections.unmodifiableMap(value);
    }


    public String getDescription() {
        return String.format(super.getDescription() + ": DataMap size %s", getValue().size());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataMap dataMap)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(value, dataMap.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
