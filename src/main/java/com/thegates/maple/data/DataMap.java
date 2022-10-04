package com.thegates.maple.data;

import com.thegates.maple.exception.RequireFieldException;
import com.thegates.maple.exception.RequireTypeException;

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
    private Set<String> keys;

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
            keys = new HashSet<>(initialCapacity);
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

    public DataContainer getFirst(List<String> keys) {
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

    public DataMap requireKey(String key) throws RequireFieldException {
        if (!hasKey(key)) throw new RequireFieldException(this, key);
        return this;
    }

    public DataMap requireKeys(List<String> keys) throws RequireFieldException {
        keys.forEach(this::requireKey);
        return this;
    }

    public DataMap requireOf(String key, Class<?> clazz) throws RequireTypeException {
        requireKey(key);
        final DataContainer container = get(key);
        if (!container.isValueOf(clazz)) throw new RequireTypeException(container, clazz);
        return this;
    }

    public String getString(String key, String def) {
        return get(key).stringValue(def);
    }

    public String getString(String key) {
        return get(key).stringValue();
    }

    public boolean getBoolean(String key, boolean def) {
        return get(key).booleanValue(def);
    }

    public boolean getBoolean(String key) {
        return get(key).booleanValue();
    }

    public int getInt(String key, int def) {
        return get(key).intValue(def);
    }

    public int getInt(String key) {
        return get(key).intValue();
    }

    public double getDouble(String key, double def) {
        return get(key).doubleValue(def);
    }

    public double getDouble(String key) {
        return get(key).doubleValue();
    }

    public float getFloat(String key, float def) {
        return get(key).floatValue(def);
    }

    public float getFloat(String key) {
        return get(key).floatValue();
    }

    public long getLong(String key, long def) {
        return get(key).longValue(def);
    }

    public long getLong(String key) {
        return get(key).longValue();
    }

    public boolean hasKey(String key) {
        if (keys == null) return false;
        return keys.contains(key);
    }

    public int size() {
        if (value == null) return 0;
        return value.size();
    }

    public boolean isPresent() {
        return value != null && !value.isEmpty();
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
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
        int result = super.hashCode();
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (keys != null ? keys.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return value == null ? "emptyMap" : "dataMap with \n\t" + String.join("\n", value.entrySet().stream().map(e -> (e.getKey() + ": " + e.getValue().toString())).toList());
    }
}
