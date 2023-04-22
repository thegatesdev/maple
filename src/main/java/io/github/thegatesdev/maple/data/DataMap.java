package io.github.thegatesdev.maple.data;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;

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
 * A map element backed by a LinkedHashMap, with String for keys and DataElements for values.
 * It allows for more advanced iteration, for example by element type ({@link DataMap#iterator(Class)}.
 */
public class DataMap extends DataElement implements MappedElement {

    private final IntFunction<Map<String, DataElement>> mapSupplier;
    private Map<String, DataElement> value;

    private String keyCache;
    private DataElement elementCache;

    /**
     * Constructs an empty DataMap with its data unset.
     */
    public DataMap() {
        this(null, null);
    }

    /**
     * Constructs an empty DataMap with its parent defaulted to {@code null}.
     *
     * @param name        The name to initialize the data with.
     * @param mapSupplier An IntFunction to supply a map when initializing, taking an initial capacity.
     */
    public DataMap(String name, IntFunction<Map<String, DataElement>> mapSupplier) {
        if (name != null) setData(null, name);
        this.mapSupplier = mapSupplier;
    }

    /**
     * Constructs an empty DataMap with its parent defaulted to {@code null}.
     *
     * @param name The name to initialize the data with.
     */
    public DataMap(String name) {
        this(name, null);
    }

    /**
     * Constructs an empty DataMap with its data unset.
     *
     * @param mapSupplier An IntFunction to supply a map when initializing, taking an initial capacity.
     */
    public DataMap(IntFunction<Map<String, DataElement>> mapSupplier) {
        this(null, mapSupplier);
    }

    /**
     * Read a Map to a DataMap.
     *
     * @param data The map to read from.
     * @return A new DataMap containing all the entries of the Map,
     * the values read using {@link DataElement#readOf(Object)}
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
     * @return The previous value associated with key, or null if there was no mapping for key.
     * @throws NullPointerException     If the element or key is null.
     * @throws IllegalArgumentException When the data of the input element is already set.
     */
    public DataElement put(String key, DataElement element) throws RuntimeException {
        if (key == null) throw new NullPointerException("key can't be null");
        if (element == null) throw new NullPointerException("element can't be null");
        if (element.isDataSet())
            throw new IllegalArgumentException("This element already has a parent / name. Did you mean to copy() first?");
        if (value == null) init(1);
        value.put(key, element.setData(this, key));
        keyCache = key;
        elementCache = element;
        return this;
    }

    private void init(int initialCapacity) {
        if (value == null) {
            if (mapSupplier == null) value = new LinkedHashMap<>(initialCapacity);
            else {
                final Map<String, DataElement> suppliedMap = mapSupplier.apply(initialCapacity);
                if (!suppliedMap.isEmpty())
                    throw new IllegalArgumentException("Map supplier should return an empty map");
                value = suppliedMap;
            }
        }
    }

    /**
     * Read a Map with unknown type keys to a DataMap.
     *
     * @param data The map to read from.
     * @return A new DataMap containing the entries of the Map which key is a String,
     * the values read using {@link DataElement#readOf(Object)}
     */
    public static DataMap readUnknown(Map<?, ?> data) {
        final DataMap output = new DataMap();
        for (Map.Entry<?, ?> entry : data.entrySet()) {
            if (entry.getKey() instanceof String key) output.put(key, DataElement.readOf(entry.getValue()));
        }
        return output;
    }

    /**
     * @param elementClass The class of DataElements to collect.
     * @param <E>          The type of DataElements to collect.
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
     * @param elementClass The class to create an iterator for.
     * @param <E>          The type to create an iterator for.
     * @return The iterator for entries with values of this {@code elementClass}.
     */
    @Override
    public <E extends DataElement> Iterator<Map.Entry<String, E>> iterator(Class<E> elementClass) {
        return new ClassedIterator<>(elementClass);
    }

    /**
     * Get the element associated with this key, or null.
     *
     * @param key The key of the element.
     * @return The element associated with this key, or {@code null}.
     */
    @Override
    public DataElement getOrNull(String key) {
        if (Objects.equals(keyCache, key)) return elementCache;
        if (value != null) {
            keyCache = key;
            return elementCache = value.get(key);
        }
        return null;
    }

    /**
     * Checks if this map contains all supplied keys.
     *
     * @param keys The keys to check for.
     * @return {@code true} if all of the keys were contained in this map.
     */
    @Override
    public boolean hasKeys(Collection<String> keys) {
        if (value == null) return false;
        return value.keySet().containsAll(keys);
    }

    /**
     * @return The size of this map, or {@code 0} if the map is not initialized.
     */
    @Override
    public int size() {
        if (value == null) return 0;
        return value.size();
    }

    /**
     * @return A DataArray with the *cloned* values of this DataMap, with the same ordering.
     */
    @Override
    public DataArray valueArray() {
        return DataArray.cloneFrom(value.values());
    }

    /**
     * @return A DataList with the *cloned* values of this DataMap, with the same ordering.
     */
    @Override
    public DataList valueList() {
        return new DataList().cloneFrom(value.values());
    }

    @Override
    public Iterator<DataElement> iterator() {
        return value.values().iterator();
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
    public Map<String, DataElement> value() {
        if (value == null) return Collections.emptyMap();
        return Collections.unmodifiableMap(value);
    }

    @Override
    public DataMap asMap() {
        return this;
    }

    @Override
    public void ifMap(final Consumer<DataMap> mapConsumer, final Runnable elseAction) {
        mapConsumer.accept(this);
    }

    @Override
    public boolean isMap() {
        return true;
    }

    /**
     * Check if this list is empty, or not initialized.
     */
    @Override
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    @Override
    public DataMap name(String name) throws IllegalArgumentException {
        super.name(name);
        return this;
    }

    @Override
    protected Map<String, DataElement> raw() {
        return value;
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

    /**
     * @param toAdd The DataMap to add the elements from to this map.
     * @return This DataMap.
     * @see DataMap#cloneFrom(Map)
     */
    public DataMap cloneFrom(DataMap toAdd) {
        return cloneFrom(toAdd.value);
    }

    /**
     * Clone the elements of the input map to this map.
     *
     * @param toAdd The input map.
     * @return This DataMap.
     */
    public DataMap cloneFrom(Map<String, DataElement> toAdd) {
        if (value == null) init(toAdd.size());
        for (Map.Entry<String, DataElement> entry : toAdd.entrySet()) {
            put(entry.getKey(), entry.getValue().clone());
        }
        return this;
    }

    private class ClassedIterator<E extends DataElement> implements Iterator<Map.Entry<String, E>> {
        private final Class<E> elementClass;
        private final Iterator<Map.Entry<String, DataElement>> iterator;
        private Map.Entry<String, E> next;

        public ClassedIterator(Class<E> elementClass) {
            this.elementClass = elementClass;
            iterator = value.entrySet().iterator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean hasNext() {
            if (!iterator.hasNext()) return false;
            final Map.Entry<String, DataElement> el = iterator.next();
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
