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
            data.forEach((s, o) -> output.put(s, DataElement.readOf(o)));
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
        final DataElement el = get(key);
        return el.isDataPrimitive() ? el.getAsDataPrimitive().getValueUnsafe() : null;
    }

    public String getString(String key, String def) {
        final DataElement el = get(key);
        return el.isDataPrimitive() ? el.getAsDataPrimitive().stringValue(def) : def;
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public boolean getBoolean(String key, boolean def) {
        final DataElement el = get(key);
        return el.isDataPrimitive() ? el.getAsDataPrimitive().booleanValue(def) : def;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public int getInt(String key, int def) {
        final DataElement el = get(key);
        return el.isDataPrimitive() ? el.getAsDataPrimitive().intValue(def) : def;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public double getDouble(String key, double def) {
        final DataElement el = get(key);
        return el.isDataPrimitive() ? el.getAsDataPrimitive().doubleValue(def) : def;
    }

    public double getDouble(String key) {
        return getDouble(key, 0D);
    }

    public float getFloat(String key, float def) {
        final DataElement el = get(key);
        return el.isDataPrimitive() ? el.getAsDataPrimitive().floatValue(def) : def;
    }

    public float getFloat(String key) {
        return getFloat(key, 0F);
    }

    public long getLong(String key, long def) {
        final DataElement el = get(key);
        return el.isDataPrimitive() ? el.getAsDataPrimitive().longValue(def) : def;
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    //--

    public void put(String key, DataElement container) {
        if (key == null) throw new NullPointerException("key can't be null");
        if (container == null) throw new NullPointerException("element can't be null");
        if (value == null) {
            init(1);
        }
        value.put(key, container.setName(key).setParent(this));
        keys.add(key);
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
        final DataElement el = value.get(key);
        if (el != null) action.accept(el);
    }

    @SuppressWarnings("unchecked")
    public <T extends DataElement> void doIfPresent(String key, Class<T> clazz, Consumer<T> action) {
        final DataElement el = value.get(key);
        if (clazz.isInstance(el)) action.accept((T) el);
    }

    public DataElement navigate(String[] keys) {
        if (keys.length == 0) return new DataNull(this, null);
        final DataElement el = get(keys[0]);
        if (!el.isDataMap()) return new DataNull(this, keys[0]);
        return el.getAsDataMap().navigate(Arrays.copyOfRange(keys, 1, keys.length));
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
