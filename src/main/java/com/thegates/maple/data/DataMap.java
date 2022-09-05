package com.thegates.maple.data;

import java.util.*;
import java.util.function.Consumer;

/*
Copyright (C) 2022  Timar Karels

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class DataMap extends DataElement {

    private Map<String, DataContainer> value;
    private List<String> keys;

    private byte stateCheck = 0;

    public DataMap() {
    }

    public DataMap(int initialCapacity) {
        init(initialCapacity);
    }

    public static synchronized DataMap read(Map<?, ?> data) {
        DataMap output = new DataMap();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (entry.getKey() instanceof String key) {
                Object value = entry.getValue();
                DataContainer dataContainer = DataContainer.read(value);
                output.put(key, dataContainer);
            }
        }
        return output;
    }

    @Override
    public DataMap setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public DataMap setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    private void init(int initialCapacity) {
        if (value == null) {
            value = new HashMap<>(initialCapacity);
            keys = new ArrayList<>(initialCapacity);
        }
    }


    public Map<String, DataContainer> getValue() {
        if (value == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(value);
    }

    public DataContainer get(String key) {
        if (value == null) {
            return new DataContainer();
        }
        DataContainer container = value.get(key);
        return container != null ? container : new DataContainer();
    }

    public DataContainer get(List<String> keys) {
        if (!(value == null || this.keys.isEmpty())) {
            for (String string : keys) {
                DataContainer c = get(string);
                if (c != null) return c;
            }
        }
        return new DataContainer();
    }

    public boolean hasKey(String key) {
        if (keys == null) return false;
        return keys.contains(key);
    }

    public int size() {
        return value.size();
    }

    @Override
    public DataElement copy() {
        return new DataMap().putAll(this);
    }


    void put(String key, DataContainer container) {
        if (value == null) {
            init(1);
        }
        value.put(key, container.copy().setName(key).setParent(this));
        keys.add(key);
        stateCheck((byte) 3);
    }

    public void put(String key, Object object) {
        put(key, new DataContainer(object));
    }

    public synchronized DataMap putAll(DataMap dataMap) {
        if (value == null) init(dataMap.size());
        dataMap.getValue().forEach(this::put);
        stateCheck((byte) 8);
        return this;
    }

    public void doIfPresent(String key, Consumer<DataContainer> action) {
        if (hasKey(key)) {
            action.accept(get(key));
        }
        stateCheck((byte) 16);
    }

    public <T> void doIfPresent(String key, Class<T> clazz, Consumer<T> action) {
        if (hasKey(key)) {
            T val = get(key).getValueOrNull(clazz);
            if (val != null)
                action.accept(val);
        }
        stateCheck((byte) 16);
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
