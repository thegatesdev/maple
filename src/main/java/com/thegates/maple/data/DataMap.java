package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;

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

    private LinkedHashMap<String, DataElement> value;

    public DataMap() {
    }

    public DataMap(String name) {
        setData(null, name);
    }

    public DataMap(String name, int initialCapacity) {
        super(name);
        init(initialCapacity);
    }

    private void init(int initialCapacity) {
        if (value == null) value = new LinkedHashMap<>(initialCapacity);
    }

    public DataMap(int initialCapacity) {
        init(initialCapacity);
    }

    static DataMap readInternal(Map<?, ?> data) {
        final DataMap output = new DataMap();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (entry.getKey() instanceof String key) output.put(key, DataElement.readOf(entry.getValue()));
        }
        return output;
    }

    public DataMap put(String key, DataElement element) throws RuntimeException {
        if (key == null) throw new NullPointerException("key can't be null");
        if (element == null) throw new NullPointerException("element can't be null");
        if (element.isDataSet())
            throw new IllegalArgumentException("This element already has a parent / name. Did you mean to copy() first?");
        if (value == null) init(1);
        synchronized (MODIFY_MUTEX) {
            value.put(key, element.setData(this, key));
        }
        return this;
    }

    public static DataMap read(Map<String, ?> data) {
        final DataMap output = new DataMap();
        for (Map.Entry<String, ?> entry : data.entrySet()) {
            output.put(entry.getKey(), DataElement.readOf(entry.getValue()));
        }
        return output;
    }

    public <E extends DataElement> Map<String, E> collect(Class<E> elementClass) {
        final ArrayList<Map.Entry<String, E>> collector = new ArrayList<>();
        iterator(elementClass).forEachRemaining(collector::add);
        final LinkedHashMap<String, E> out = new LinkedHashMap<>(collector.size(), 1f);
        out.entrySet().addAll(collector);
        return out;
    }

    public <E extends DataElement> Iterator<Map.Entry<String, E>> iterator(Class<E> elementClass) {
        return new ClassedIterator<>(elementClass);
    }

    public <P> Map<String, P> collectPrimitive(Class<P> primitiveClass) {
        final Map<String, P> out = new LinkedHashMap<>(size());
        iterator(DataPrimitive.class).forEachRemaining(e -> {
            final P val = e.getValue().valueOrNull(primitiveClass);
            if (val != null) out.put(e.getKey(), val);
        });
        return out;
    }

    public int size() {
        if (value == null) return 0;
        return value.size();
    }

    public <P> P get(String key, Class<P> primitiveClass) {
        return getPrimitive(key).requireValue(primitiveClass);
    }

    public DataPrimitive getPrimitive(String key) {
        return get(key).requireOf(DataPrimitive.class);
    }

    public DataElement get(String key) {
        final DataElement el = getOrNull(key);
        return el == null ? new DataNull(this, key) : el;
    }

    public DataElement getOrNull(String key) {
        if (value != null) synchronized (READ_MUTEX) {
            return value.get(key);
        }
        return null;
    }

    public boolean getBoolean(String key) {
        return getPrimitive(key).booleanValue();
    }

    public boolean getBoolean(String key, boolean def) {
        return hasKey(key) ? getPrimitive(key).booleanValue() : def;
    }

    public boolean hasKey(String key) {
        if (value == null) return false;
        return value.containsKey(key);
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

    public int getInt(String key) {
        return getPrimitive(key).intValue();
    }

    public int getInt(String key, int def) {
        return hasKey(key) ? getPrimitive(key).intValue() : def;
    }

    public DataList getList(String key) {
        return get(key).requireOf(DataList.class);
    }

    public long getLong(String key) {
        return getPrimitive(key).longValue();
    }

    public long getLong(String key, long def) {
        return hasKey(key) ? getPrimitive(key).longValue() : def;
    }

    public DataMap getMap(String key) {
        return get(key).requireOf(DataMap.class);
    }

    public String getString(String key) {
        return getPrimitive(key).stringValue();
    }

    public String getString(String key, String def) {
        return hasKey(key) ? getPrimitive(key).stringValue() : def;
    }

    public <P> P getUnsafe(String key) {
        return getPrimitive(key).valueUnsafe();
    }

    public <P> P getUnsafe(String key, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isPrimitive()) return def;
        if (def == null) return el.asPrimitive().valueUnsafe();
        final P val = el.asPrimitive().valueUnsafe();
        return val == null ? def : val;
    }

    public boolean hasKeys(String... keys) {
        if (value == null) return false;
        return hasKeys(Arrays.asList(keys));
    }

    public boolean hasKeys(Collection<String> keys) {
        if (value == null) return false;
        return value.keySet().containsAll(keys);
    }

    public void ifPresent(String key, Consumer<DataElement> action) {
        final DataElement el = getOrNull(key);
        if (el != null) action.accept(el);
    }

    public <E extends DataElement> void ifPresent(String key, Class<E> elementClass, Consumer<E> action) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isOf(elementClass)) action.accept(el.asUnsafe(elementClass));
    }

    public <P> void ifPrimitive(String key, Class<P> primitiveClass, Consumer<P> action) {
        final P p = get(key, primitiveClass, null);
        if (p != null) action.accept(p);
    }

    public <P> P get(String key, Class<P> primitiveClass, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isPrimitive()) return def;
        if (def == null) // Shortcut, def is already null, so returning null wouldn't matter.
            return el.asPrimitive().valueOrNull(primitiveClass);
        final P val = el.asPrimitive().valueOrNull(primitiveClass);
        return val == null ? def : val;
    }

    public DataElement navigate(String... keys) {
        return navigate(0, keys);
    }

    private DataElement navigate(int current, String[] keys) {
        final String key = keys[current];
        final DataElement element = get(key);
        if (current == keys.length - 1) return element;
        if (!element.isMap()) return new DataNull(this, key);
        return element.asMap().navigate(++current, keys);
    }

    public DataMap requireKey(String key) throws ReadException {
        if (!hasKey(key)) throw ReadException.requireField(this, key);
        return this;
    }

    public DataMap requireKeys(String... keys) throws ReadException {
        return requireKeys(Arrays.asList(keys));
    }

    public DataMap requireKeys(Collection<String> keys) throws ReadException {
        if (!hasKeys(keys)) throw ReadException.requireField(this, String.join(" and ", keys));
        return this;
    }

    public DataMap requireOf(String key, Class<? extends DataElement> clazz) throws ReadException {
        final DataElement el = getOrNull(key);
        if (!(clazz.isInstance(el))) throw ReadException.requireType(el, clazz);
        return this;
    }

    @Override
    public Iterator<Map.Entry<String, DataElement>> iterator() {
        return value.entrySet().iterator();
    }

    @Override
    public Spliterator<Map.Entry<String, DataElement>> spliterator() {
        return value.entrySet().spliterator();
    }

    @Override
    public String toString() {
        if (value == null || value.isEmpty()) return "emptyMap";
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("dataMap{");
        int len = value.size();
        for (Map.Entry<String, DataElement> entry : this) {
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

    public Map<String, DataElement> value() {
        if (value == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(value);
    }

    @Override
    public DataMap asMap() {
        return this;
    }

    @Override
    public boolean isList() {
        return false;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataMap)) return false;
        return super.equals(o);
    }

    @Override
    public DataMap clone() {
        return new DataMap().cloneFrom(this);
    }

    @Override
    protected LinkedHashMap<String, DataElement> raw() {
        return value;
    }

    public DataMap cloneFrom(DataMap toAdd) {
        return cloneFrom(toAdd.value);
    }

    public DataMap cloneFrom(Map<String, DataElement> toAdd) {
        if (value == null) init(toAdd.size());
        for (Map.Entry<String, DataElement> entry : toAdd.entrySet()) {
            synchronized (MODIFY_MUTEX) {
                put(entry.getKey(), entry.getValue().clone());
            }
        }
        return this;
    }

    private class ClassedIterator<E extends DataElement> implements Iterator<Map.Entry<String, E>> {
        private final Class<E> elementClass;
        private final Iterator<Map.Entry<String, DataElement>> iterator;
        private Map.Entry<String, E> next;

        public ClassedIterator(Class<E> elementClass) {
            this.elementClass = elementClass;
            iterator = iterator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasNext() {
            if (!iterator.hasNext()) return false;
            final Map.Entry<String, DataElement> el;
            synchronized (READ_MUTEX) {
                el = iterator.next();
            }
            if (el.getValue().isOf(elementClass)) {
                next = ((Map.Entry<String, E>) el);
                return true;
            }
            return false;
        }

        @Override
        public Map.Entry<String, E> next() {
            return next;
        }
    }
}
