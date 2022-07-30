package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;

import java.util.*;
import java.util.function.Consumer;

public class DataMap extends DataElement {

    private final Map<String, DataContainer> value;
    private final List<String> keys;

    public DataMap() {
        this(1);
    }

    public DataMap(int initialCapacity) {
        value = new HashMap<>(initialCapacity);
        keys = new ArrayList<>(initialCapacity);
    }

    public static synchronized DataMap of(Map<?, ?> data) {
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
            return emptyContainer();
        }
        DataContainer container = getValue().get(key);
        return container != null ? container : emptyContainer().setName(key);
    }

    public DataContainer get(DataList keys) {
        List<String> strings = keys.getAsListOf(String.class);
        if (strings.isEmpty()) return emptyContainer();
        for (String string : strings) {
            DataContainer c = get(string);
            if (c.isPresent()) return c;
        }
        return emptyContainer();
    }

    private DataContainer emptyContainer() {
        return new DataContainer().setParent(this).setName("none");
    }

    public void put(String key, DataContainer container) {
        value.put(key, container.setParent(this).setName(key));
        keys.add(key);
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
        return keys.contains(key);
    }

    public Map<String, DataContainer> getValue() {
        return Collections.unmodifiableMap(value);
    }


    public DataMap requireKey(String key) {
        if (!keys.contains(key)) throw new ReadException(this, "map requires field of key " + key);
        return this;
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
