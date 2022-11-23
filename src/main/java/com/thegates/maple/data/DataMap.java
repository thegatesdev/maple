package com.thegates.maple.data;

import com.thegates.maple.exception.ElementException;

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

/**
 * A DataMap is a map element backed by a LinkedHashMap, with String for keys and DataElements for values.
 * It allows for more advanced iteration, for example by element type ({@link DataMap#iterator(Class)}.
 */
public class DataMap extends DataElement implements Iterable<Map.Entry<String, DataElement>>, Cloneable, Comparable<DataElement> {

    private LinkedHashMap<String, DataElement> value;

    /**
     * Constructs an empty DataMap with its data unset.
     */
    public DataMap() {
    }

    /**
     * Constructs an empty DataMap with its parent defaulted to {@code null}.
     *
     * @param name The name to initialize the data with.
     */
    public DataMap(String name) {
        setData(null, name);
    }

    /**
     * Constructs an empty DataMap with its parent defaulted to {@code null}.
     *
     * @param name            The name to initialize the data with.
     * @param initialCapacity The initial capacity.
     */
    public DataMap(String name, int initialCapacity) {
        super(name);
        init(initialCapacity);
    }

    private void init(int initialCapacity) {
        if (value == null) value = new LinkedHashMap<>(initialCapacity);
    }

    /**
     * Constructs a DataMap with its data unset.
     *
     * @param initialCapacity The initial capacity.
     */
    public DataMap(int initialCapacity) {
        init(initialCapacity);
    }

    /**
     * Read a Map to a DataMap.
     *
     * @param data The map to read from.
     * @return @return A new DataMap containing all the keys and elements of the Map,
     * read using {@link DataElement#readOf(Object)}
     */
    public static DataMap read(Map<String, ?> data) {
        final DataMap output = new DataMap();
        for (Map.Entry<String, ?> entry : data.entrySet()) {
            output.put(entry.getKey(), DataElement.readOf(entry.getValue()));
        }
        return output;
    }

    /**
     * Put an element into this map.
     *
     * @param key     Key with which the specified value is to be associated.
     * @param element Element to be associated with the specified key.
     * @return This same DataMap.
     * @throws NullPointerException     If the element or key is null.
     * @throws IllegalArgumentException When the data of the input element is already set.
     */
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

    static DataMap readInternal(Map<?, ?> data) {
        final DataMap output = new DataMap();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (entry.getKey() instanceof String key) output.put(key, DataElement.readOf(entry.getValue()));
        }
        return output;
    }

    /**
     * @param elementClass The class of DataElements to
     * @return A new Map containing all the key value pairs of the values that match {@code elementClass}.
     */
    public <E extends DataElement> Map<String, E> collect(Class<E> elementClass) {
        final ArrayList<Map.Entry<String, E>> collector = new ArrayList<>();
        iterator(elementClass).forEachRemaining(collector::add);
        final LinkedHashMap<String, E> out = new LinkedHashMap<>(collector.size(), 1f);
        out.entrySet().addAll(collector);
        return out;
    }

    /**
     * Get the iterator for entries with values of this {@code elementClass}.
     */
    public <E extends DataElement> Iterator<Map.Entry<String, E>> iterator(Class<E> elementClass) {
        return new ClassedIterator<>(elementClass);
    }

    /**
     * Get the value of the {@link DataPrimitive} associated with this key.
     *
     * @param key            The key of the primitive.
     * @param primitiveClass The type the primitive should be of.
     * @return The value of the primitive.
     * @throws ElementException If the element is not present, is not a primitive, or it's value is not of the required type.
     */
    public <P> P get(String key, Class<P> primitiveClass) {
        return getPrimitive(key).requireValue(primitiveClass);
    }

    public DataPrimitive getPrimitive(String key) {
        return get(key).requireOf(DataPrimitive.class);
    }

    /**
     * Get the element associated with this key.
     *
     * @param key The key of the element.
     * @return The element associated with this key, or a new {@link DataNull};
     */
    public DataElement get(String key) {
        final DataElement el = getOrNull(key);
        return el == null ? new DataNull().setData(this, key) : el;
    }

    /**
     * Get the element associated with this key, or null.
     *
     * @param key The key of the element.
     * @return The element associated with this key, or {@code null}.
     */
    public DataElement getOrNull(String key) {
        if (value != null) synchronized (READ_MUTEX) {
            return value.get(key);
        }
        return null;
    }

    /**
     * Get the value of the {@link DataPrimitive} associated with this key, or a default.
     *
     * @param key            The key of the primitive.
     * @param primitiveClass The type the primitive should be of.
     * @param def            The default value to return when the element is not present, not a primitive, or the type does not match.
     * @return The value of the primitive, or the default value.
     */
    public <P> P get(String key, Class<P> primitiveClass, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isPrimitive()) return def;
        if (def == null) // Shortcut, def is already null, so returning null wouldn't matter.
            return el.asPrimitive().valueOrNull(primitiveClass);
        final P val = el.asPrimitive().valueOrNull(primitiveClass);
        return val == null ? def : val;
    }

    public boolean getBoolean(String key) {
        return getPrimitive(key).booleanValue();
    }

    public boolean getBoolean(String key, boolean def) {
        return hasKey(key) ? getPrimitive(key).booleanValue() : def;
    }

    /**
     * Check if this map contains this key.
     *
     * @param key The key to check for.
     */
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

    /**
     * Get the value of the first {@link DataPrimitive} associated with one of these keys.
     *
     * @param keys           The possible keys of the primitive.
     * @param primitiveClass The type the primitive should be of.
     * @return The value of the primitive.
     * @throws ElementException If the found element is not present, is not a primitive, or it's value is not of the required type.
     */
    public <P> P getFirst(Class<P> primitiveClass, String... keys) {
        for (String key : keys) {
            final DataElement el = getOrNull(key);
            if (el != null && el.isPrimitive()) return el.asPrimitive().requireValue(primitiveClass);
        }
        throw ElementException.requireField(this, String.join(" or ", keys));
    }

    /**
     * Find the first element associated with one of the keys.
     *
     * @param keys The possible keys of the element.
     * @return The element associated with one of these keys, or a new {@link DataNull};
     */
    public DataElement getFirst(String... keys) {
        for (String key : keys) {
            final DataElement el = getOrNull(key);
            if (el != null) return el;
        }
        return new DataNull().setData(this, null);
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

    /**
     * Checks if this map contains all supplied keys.
     *
     * @param keys The keys to check for.
     * @return {@code true} if all of the keys were contained in this map.
     */
    public boolean hasKeys(String... keys) {
        if (value == null) return false;
        return hasKeys(Arrays.asList(keys));
    }

    /**
     * Checks if this map contains all supplied keys.
     *
     * @param keys The keys to check for.
     * @return {@code true} if all of the keys were contained in this map.
     */
    public boolean hasKeys(Collection<String> keys) {
        if (value == null) return false;
        return value.keySet().containsAll(keys);
    }

    /**
     * Runs the action if the specified element is present, and is a DataList.
     * This will never be a DataNull.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifList(String key, Consumer<DataList> action) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isList()) action.accept(el.asList());
    }

    /**
     * Runs the action if the specified element is present, and is a DataMap.
     * This will never be a DataNull.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifMap(String key, Consumer<DataMap> action) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isMap()) action.accept(el.asMap());
    }

    /**
     * Runs the action if the specified element is present.
     * This will never be a DataNull.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifPresent(String key, Consumer<DataElement> action) {
        final DataElement el = getOrNull(key);
        if (el != null) action.accept(el);
    }

    /**
     * Runs the action if the specified element is present, and is a DataPrimitive.
     * This will never be a DataNull.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifPrimitive(String key, Consumer<DataPrimitive> action) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isPrimitive()) action.accept(el.asPrimitive());
    }

    /**
     * Runs the action if the specified element is present, is a DataPrimitive, and it's value is of the specified type.
     * This will never be a DataNull.
     *
     * @param key            The key to find the element at.
     * @param primitiveClass The type the DataPrimitive should be of.
     * @param action         The consumer to run when the element is found.
     */
    public <P> void ifPrimitiveOf(String key, Class<P> primitiveClass, Consumer<P> action) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isPrimitive()) {
            final DataPrimitive primitive = el.asPrimitive();
            if (primitive.isValueOf(primitiveClass)) action.accept(primitive.valueUnsafe());
        }
    }

    /**
     * Navigate this map with the specified keys until the element is reached, or the next element is not a map.
     *
     * @param keys The keys to navigate the elements with.
     * @return The found DataElement, or {@code null} if it was not found.
     */
    public DataElement navigate(String... keys) {
        return navigate(0, keys);
    }

    private DataElement navigate(int current, String[] keys) {
        final String key = keys[current];
        final DataElement element = get(key);
        if (current == keys.length - 1) return element;
        if (!element.isMap()) return new DataNull().setData(this, key);
        return element.asMap().navigate(++current, keys);
    }

    /**
     * @param primitiveClass The class the DataPrimitives values should be of.
     * @return A new Map containing the key values pairs of the DataPrimitives in this DataMap conforming to {@code elementClass}.
     */
    public <P> Map<String, P> primitiveMap(Class<P> primitiveClass) {
        final Map<String, P> out = new LinkedHashMap<>(size());
        iterator(DataPrimitive.class).forEachRemaining(e -> {
            final P val = e.getValue().valueOrNull(primitiveClass);
            if (val != null) out.put(e.getKey(), val);
        });
        return out;
    }

    /**
     * @return The size of this map, or {@code 0} if the map is not initialized.
     */
    public int size() {
        if (value == null) return 0;
        return value.size();
    }

    /**
     * Check if this key is present, or else throw.
     *
     * @param key The key to check for.
     * @throws ElementException When this key isn't present.
     */
    public DataMap requireKey(String key) throws ElementException {
        if (!hasKey(key)) throw ElementException.requireField(this, key);
        return this;
    }

    /**
     * Check if all these keys are present, or else throw.
     *
     * @param keys The keys to check for.
     * @throws ElementException When these keys aren't present.
     */
    public DataMap requireKeys(String... keys) throws ElementException {
        return requireKeys(Arrays.asList(keys));
    }

    /**
     * Check if all these keys are present, or else throw.
     *
     * @param keys The keys to check for.
     * @throws ElementException When these keys aren't present.
     */
    public DataMap requireKeys(Collection<String> keys) throws ElementException {
        if (!hasKeys(keys)) throw ElementException.requireField(this, String.join(" and ", keys));
        return this;
    }

    /**
     * Check if the element associated with this key is an instance of {@code clazz}
     *
     * @param key   The key associated with the element.
     * @param clazz The class the element should be of.
     * @throws ElementException When the element associated with this key is not of the required type.
     */
    public DataMap requireOf(String key, Class<? extends DataElement> clazz) throws ElementException {
        final DataElement el = getOrNull(key);
        if (!(clazz.isInstance(el))) throw ElementException.requireType(el, clazz);
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

    @Override
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

    /**
     * Check if this list is empty, or not initialized.
     */
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

    /**
     * @see DataMap#cloneFrom(Map)
     */
    public DataMap cloneFrom(DataMap toAdd) {
        return cloneFrom(toAdd.value);
    }

    /**
     * Clone the elements of the input map to this map.
     *
     * @return This DataMap.
     */
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
