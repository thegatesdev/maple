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

public class DataMap extends DataElement implements Iterable<Map.Entry<String, DataElement>>, Cloneable, Comparable<DataElement> {

    private Map<String, DataElement> value;

    public DataMap() {
    }

    protected DataMap(DataElement parent, String name) {
        super(parent, name);
    }

    protected DataMap(String name) {
        super(name);
    }

    protected DataMap(DataElement parent, String name, int initialCapacity) {
        this(parent, name);
        init(initialCapacity);
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
        if (value == null) value = new LinkedHashMap<>(initialCapacity);
    }


    // --


    public Map<String, DataElement> getValue() {
        if (value == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(value);
    }

    @Override
    protected Object value() {
        return value;
    }

    public DataElement get(String key) {
        if (value != null) {
            final DataElement el = value.get(key);
            if (el != null) return el;
        }
        return new DataNull(this, key);
    }

    public DataElement getOrNull(String key) {
        if (value != null) return value.get(key);
        return null;
    }

    // --

    /**
     * Primitive getter.
     */
    public <T> T getUnsafe(String key) {
        return getPrimitive(key).getValueUnsafe();
    }

    /**
     * Primitive getter.
     */
    public <T> T get(String key, Class<T> dataClass) {
        return get(key).requireOf(DataPrimitive.class).requireValue(dataClass);
    }

    /**
     * Primitive getter.
     */
    public <T> T getOrNull(String key, Class<T> dataClass) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isDataPrimitive()) return null;
        return el.getAsDataPrimitive().getValueOrNull(dataClass);
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


    public DataMap put(String key, DataElement element) throws RuntimeException {
        if (key == null) throw new NullPointerException("key can't be null");
        if (element == null) throw new NullPointerException("element can't be null");
        if (element.hasDataSet()) throw new IllegalArgumentException("This element already has a parent / name. Did you mean to copy() first?");
        if (value == null) init(1);
        value.put(key, element.setData(this, key));
        return this;
    }

    public DataMap putAll(DataMap dataMap) {
        final var toAdd = dataMap.getValue();
        if (value == null) init(toAdd.size());
        synchronized (MODIFY_MUTEX) {
            // Don't use addAll because parent will not be set properly.
            toAdd.forEach(this::put);
        }
        return this;
    }


    public void doIfPresent(String key, Consumer<DataElement> action) {
        final DataElement element = getOrNull(key);
        if (element != null) action.accept(element);
    }

    public <E extends DataElement> void doIfPresent(String key, Class<E> clazz, Consumer<E> action) {
        final DataElement el = getOrNull(key);
        if (el == null) return;
        final E typed = el.getAsOrNull(clazz);
        if (typed != null) action.accept(typed);
    }


    public DataElement navigate(String... keys) {
        return navigate(0, keys);
    }

    private DataElement navigate(int current, String[] keys) {
        final String key = keys[current];
        final DataElement element = get(key);
        if (current == keys.length - 1) return element;
        if (!element.isDataMap()) return new DataNull(this, key);
        return element.getAsDataMap().navigate(++current, keys);
    }


    @SuppressWarnings("unchecked")
    public <E extends DataElement> Map<String, E> collect(Class<E> elementClass) {
        final LinkedList<Map.Entry<String, E>> entries = new LinkedList<>();
        for (Map.Entry<String, DataElement> e : this) {
            if (e.getValue().isOf(elementClass))
                entries.add((Map.Entry<String, E>) e);
        }
        final Map<String, E> out = new LinkedHashMap<>(entries.size(), 1);
        out.entrySet().addAll(entries);
        return out;
    }

    public <P> Map<String, P> collectPrimitive(Class<P> primitiveClass) {
        final Map<String, P> out = new LinkedHashMap<>(size());
        for (Map.Entry<String, DataElement> e : this) {
            if (e.getValue().isOf(DataPrimitive.class)) {
                final P valueOrNull = e.getValue().getAsDataPrimitive().getValueOrNull(primitiveClass);
                if (valueOrNull != null) out.put(e.getKey(), valueOrNull);
            }
        }
        return out;
    }


    // --


    public DataMap requireKey(String key) throws RequireFieldException {
        if (!hasKey(key)) throw new RequireFieldException(this, key);
        return this;
    }

    public DataMap requireKeys(Collection<String> keys) throws RequireFieldException {
        if (!hasKeys(keys)) throw new RequireFieldException(this, String.join(" and ", keys));
        return this;
    }

    public DataMap requireKeys(String... keys) throws RequireFieldException {
        return requireKeys(Arrays.asList(keys));
    }

    public DataMap requireOf(String key, Class<? extends DataElement> clazz) throws RequireTypeException {
        requireKey(key);
        final DataElement el = get(key);
        if (!(clazz.isInstance(el))) throw new RequireTypeException(el, clazz);
        return this;
    }

    public boolean hasKey(String key) {
        if (value == null) return false;
        return value.containsKey(key);
    }

    public boolean hasKeys(Collection<String> keys) {
        if (value == null) return false;
        return value.keySet().containsAll(keys);
    }

    public boolean hasKeys(String... keys) {
        if (value == null) return false;
        return hasKeys(Arrays.asList(keys));
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
    public DataElement clone() {
        return new DataMap().putAll(this);
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
        if (!(o instanceof DataMap)) return false;
        return super.equals(o);
    }


    @Override
    public String toString() {
        if (value == null || value.isEmpty()) return "emptyMap";
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dataMap{");
        int len = value.size();
        for (Map.Entry<String, DataElement> entry : value.entrySet()) {
            stringBuilder.append("'");
            stringBuilder.append(entry.getKey());
            stringBuilder.append("'");
            stringBuilder.append(": ");
            stringBuilder.append(entry.getValue());
            if (--len > 0) stringBuilder.append(", ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Override
    public Iterator<Map.Entry<String, DataElement>> iterator() {
        return value.entrySet().iterator();
    }

    @Override
    public Spliterator<Map.Entry<String, DataElement>> spliterator() {
        return value.entrySet().spliterator();
    }
}
