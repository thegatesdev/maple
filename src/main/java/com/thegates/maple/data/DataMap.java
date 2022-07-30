package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;

import java.util.*;
import java.util.function.Consumer;

public class DataMap extends DataElement {

    private Map<String, DataContainer> value;
    private List<String> keys;

    private byte stateCheck = 0;

    public DataMap() {
    }

    public DataMap(int initialCapacity) {
        initialise(initialCapacity);
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

    private void initialise(int initialCapacity) {
        if (value == null) {
            value = new HashMap<>(initialCapacity);
            keys = new ArrayList<>(initialCapacity);
        }
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
        if (key == null || value == null) {
            return emptyContainer();
        }
        DataContainer container = value.get(key);
        return container != null ? container : emptyContainer().setName(key);
    }

    public DataContainer get(DataList keys) {
        if (!(value == null || keys.isEmpty())) {
            List<String> strings = keys.getAsListOf(String.class);
            for (String string : strings) {
                DataContainer c = get(string);
                if (c.isPresent()) return c;
            }
        }
        return emptyContainer();
    }

    private DataContainer emptyContainer() {
        return new DataContainer().setParent(this).setName("none");
    }


    private void put(String key, DataContainer container) {
        if (value == null) {
            initialise(1);
        }
        value.put(key, container.setParent(this).setName(key));
        keys.add(key);
        stateCheck((byte) 3);
    }

    public synchronized void putAll(DataMap dataMap) {
        if (value == null) initialise(dataMap.size());
        dataMap.getValue().forEach(this::put);
        stateCheck((byte) 8);
    }

    public void doIfPresent(String key, Consumer<DataContainer> action) {
        if (has(key)) {
            action.accept(get(key));
        }
        stateCheck((byte) 16);
    }

    public <T> void doIfPresent(String key, Class<T> clazz, Consumer<T> action) {
        if (has(key)) {
            T val = get(key).getOrNull(clazz);
            if (val != null)
                action.accept(val);
        }
        stateCheck((byte) 16);
    }

    public boolean has(String key) {
        if (keys == null) return false;
        return keys.contains(key);
    }

    public Map<String, DataContainer> getValue() {
        if (value == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(value);
    }

    public int size() {
        return value.size();
    }

    private void stateCheck(byte prio) {
        if (prio > stateCheck) return;
        if (stateCheck >= 50) {
            stateCheck = 0;
            if ((value == null && keys != null))
                throw new IllegalStateException("map is null but keys aren't");
            if (keys != null && keys.size() != value.size())
                throw new IllegalStateException("amount of keys unequal to map keys");
        } else
            stateCheck += prio;
    }


    public DataMap requireKey(String key) {
        if (!keys.contains(key)) throw new ReadException(this, "map requires field of key " + key);
        return this;
    }

    public DataMap requireSize(int size) {
        if (value.size() != size) throw new ReadException(this, "map requires size " + size);
        return this;
    }

    public DataMap requireSizeHigher(int size) {
        if (value.size() <= size) throw new ReadException(this, "list requires size higher than " + size);
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
