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

    private Map<String, DataElement> value;
    private Set<String> keys;

    public DataMap() {
    }

    public DataMap(int initialCapacity) {
        init(initialCapacity);
    }

    static DataMap readInternal(Map<?, ?> data) {
        final DataMap output = new DataMap();
        synchronized (MODIFY_MUTEX) {
            data.forEach((o, o2) -> {
                if (o instanceof String key) output.put(key, DataElement.readOf(o2));
            });
        }
        return output;
    }

    public static DataMap read(Map<String, ?> data) {
        final DataMap output = new DataMap();
        synchronized (MODIFY_MUTEX) {
            data.forEach((s, o) -> {
                if (s != null)
                    output.put(s, DataElement.readOf(o));
            });
        }
        return output;
    }

    private void init(int initialCapacity) {
        if (value == null) {
            value = new HashMap<>(initialCapacity);
            keys = new HashSet<>(initialCapacity);
        }
    }


    // --


    public Map<String, DataElement> getValue() {
        if (value == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(value);
    }

    public DataElement get(String key) {
        if (value != null) {
            final DataElement el = value.get(key);
            if (el != null) return el;
        }
        return new DataNull(this, key);
    }

    // --

    public <T> T getUnsafe(String key) {
        return getPrimitive(key).getValueUnsafe();
    }

    public <T> T get(String key, Class<T> dataClass) {
        return get(key).requireOf(DataPrimitive.class).requireValue(dataClass);
    }


    public DataPrimitive getPrimitive(String key) {
        return get(key).requireOf(DataPrimitive.class);
    }

    public DataMap getMap(String key) {
        return get(key).requireOf(DataMap.class);
    }

    public DataList getList(String key) {
        return get(key).requireOf(DataList.class);
    }

    public String getString(String key) {
        return getPrimitive(key).stringValue();
    }

    public String getString(String key, String def) {
        return hasKey(key) ? getPrimitive(key).stringValue() : def;
    }

    public boolean getBoolean(String key) {
        return getPrimitive(key).booleanValue();
    }

    public boolean getBoolean(String key, boolean def) {
        return hasKey(key) ? getPrimitive(key).booleanValue() : def;
    }

    public int getInt(String key) {
        return getPrimitive(key).intValue();
    }

    public int getInt(String key, int def) {
        return hasKey(key) ? getPrimitive(key).intValue() : def;
    }

    public double getDouble(String key) {
        return getPrimitive(key).doubleValue();
    }

    public double getDouble(String key, double def) {
        return hasKey(key) ? getPrimitive(key).doubleValue() : def;
    }

    public float getFloat(String key) {
        return getPrimitive(key).floatValue();
    }

    public float getFloat(String key, float def) {
        return hasKey(key) ? getPrimitive(key).floatValue() : def;
    }

    public long getLong(String key) {
        return getPrimitive(key).longValue();
    }

    public long getLong(String key, long def) {
        return hasKey(key) ? getPrimitive(key).longValue() : def;
    }

    //--

    public DataMap put(String key, DataElement container) throws NullPointerException {
        if (key == null) throw new NullPointerException("key can't be null");
        if (container == null) throw new NullPointerException("element can't be null");
        if (value == null) {
            init(1);
        }
        value.put(key, container.setName(key).setParent(this));
        keys.add(key);
        return this;
    }

    public DataMap putAll(DataMap dataMap) {
        final var toAdd = dataMap.getValue();
        if (value == null) init(toAdd.size());
        synchronized (MODIFY_MUTEX) {
            toAdd.forEach(this::put);
        }
        return this;
    }

    public void doIfPresent(String key, Consumer<DataElement> action) {
        if (hasKey(key)) action.accept(get(key));
    }

    public <T> void doIfPresent(String key, Class<T> clazz, Consumer<T> action) {
        final DataElement el = get(key);
        if (!el.isDataNull() && el.isDataPrimitive()) {
            T valueOrNull = el.getAsDataPrimitive().getValueOrNull(clazz);
            if (valueOrNull != null) action.accept(valueOrNull);
        }
    }

    public DataElement navigate(String... keys) {
        return navigate(0, keys);
    }

    private DataElement navigate(int current, String[] keys) {
        if (current == keys.length - 1) return get(keys[current]);
        DataElement element = get(keys[current]);
        if (!element.isDataMap()) return new DataNull().setParent(this).setName(keys[current]);
        return element.getAsDataMap().navigate(++current, keys);
    }


    // --


    public DataMap requireKey(String key) throws RequireFieldException {
        if (!hasKey(key)) throw new RequireFieldException(this, key);
        return this;
    }

    public DataMap requireKeys(Collection<String> keys) throws RequireFieldException {
        if (!hasKeys(keys)) throw new RequireFieldException(this, String.join(" or ", keys));
        return this;
    }

    public DataMap requireOf(String key, Class<? extends DataElement> clazz) throws RequireTypeException {
        requireKey(key);
        final DataElement el = get(key);
        if (!(clazz.isInstance(el))) throw new RequireTypeException(el, clazz);
        return this;
    }

    public boolean hasKey(String key) {
        if (keys == null) return false;
        return keys.contains(key);
    }

    public boolean hasKeys(Collection<String> keys) {
        if (this.keys == null) return false;
        return this.keys.containsAll(keys);
    }


    public int size() {
        if (value == null) return 0;
        return value.size();
    }

    public boolean isPresent() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }


    // --


    @Override
    public DataElement copy() {
        return new DataMap().putAll(this);
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

    @Override
    public boolean isDataPrimitive() {
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
    public boolean isDataNull() {
        return false;
    }

    @Override
    public DataMap getAsDataMap() {
        return this;
    }


    // --


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
