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

    public DataMap() {
    }

    public DataMap(int initialCapacity) {
        init(initialCapacity);
    }

    static DataMap readInternal(Map<?, ?> data) {
        DataMap output = new DataMap();
        synchronized (MODIFY_MUTEX) {
            for (Map.Entry<?, ?> entry : data.entrySet()) {
                if (entry.getKey() instanceof String key) {
                    Object value = entry.getValue();
                    if (value == null) continue;
                    DataContainer c = DataContainer.read(value);
                    if (c == null || !c.isPresent()) continue;
                    output.put(key, c);
                }
            }
        }
        return output;
    }

    public static DataMap read(Map<String, ?> data) {
        final DataMap output = new DataMap();
        synchronized (MODIFY_MUTEX) {
            data.forEach((s, o) -> output.put(s, DataContainer.read(o)));
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
            return DataContainer.EMPTY;
        }
        DataContainer container = value.get(key);
        return container != null ? container : DataContainer.EMPTY;
    }

    public DataContainer get(List<String> keys) {
        if (!(value == null || this.keys.isEmpty())) {
            synchronized (GET_MUTEX) {
                for (String string : keys) {
                    DataContainer c = get(string);
                    if (c != null) return c;
                }
            }
        }
        return DataContainer.EMPTY;
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

    @Override
    public boolean isDataContainer() {
        return false;
    }

    @Override
    public boolean isDataList() {
        return false;
    }

    @Override
    public boolean isDataMap() {
        return true;
    }

    @Override
    public DataContainer getAsDataContainer() {
        return null;
    }

    @Override
    public DataList getAsDataList() {
        return null;
    }

    @Override
    public DataMap getAsDataMap() {
        return this;
    }


    public void put(String key, DataContainer container) {
        if (key == null) throw new NullPointerException("key was null");
        if (container == null) throw new NullPointerException("container was null");
        if (value == null) {
            init(1);
        }
        value.put(key, container.copy().setName(key).setParent(this));
        keys.add(key);
    }

    public DataMap putAll(DataMap dataMap) {
        if (value == null) init(dataMap.size());
        synchronized (MODIFY_MUTEX) {
            dataMap.getValue().forEach(this::put);
        }
        return this;
    }

    public void doIfPresent(String key, Consumer<DataContainer> action) {
        if (hasKey(key)) {
            action.accept(get(key));
        }
    }

    public <T> void doIfPresent(String key, Class<T> clazz, Consumer<T> action) {
        if (hasKey(key)) {
            T val = get(key).getValueOrNull(clazz);
            if (val != null)
                action.accept(val);
        }
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

    @Override
    public String toString() {
        return value == null ? "emptyMap" : String.join("\n", value.entrySet().stream().map(e -> (e.getKey() + ": " + e.getValue().toString())).toList());
    }
}
