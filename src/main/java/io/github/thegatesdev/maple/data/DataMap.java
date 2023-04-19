package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.exception.ElementException;

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
public class DataMap extends DataElement implements Iterable<DataElement> {

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
     * @param name The name to initialize the data with.
     */
    public DataMap(String name) {
        this(name, null);
    }

    /**
     * Constructs an empty DataMap with its parent defaulted to {@code null}.
     *
     * @param name            The name to initialize the data with.
     * @param mapSupplier An IntFunction to supply a map when initializing, taking an initial capacity.
     */
    public DataMap(String name, IntFunction<Map<String, DataElement>> mapSupplier) {
        if (name != null) setData(null, name);
        this.mapSupplier = mapSupplier;
    }

    /**
     * Constructs an empty DataMap with its data unset.
     *
     * @param mapSupplier An IntFunction to supply a map when initializing, taking an initial capacity.
     */
    public DataMap(IntFunction<Map<String, DataElement>> mapSupplier){
        this(null, mapSupplier);
    }

    private void init(int initialCapacity) {
        if (value == null){
            if (mapSupplier == null) value = new LinkedHashMap<>(initialCapacity);
            else{
                final Map<String, DataElement> suppliedMap = mapSupplier.apply(initialCapacity);
                if (!suppliedMap.isEmpty()) throw new IllegalArgumentException("List supplier should return empty list");
                value = suppliedMap;
            }
        }
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
        value.put(key, element.setData(this, key));
        keyCache = key;
        elementCache = element;
        return this;
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
    public <E extends DataElement> Iterator<Map.Entry<String, E>> iterator(Class<E> elementClass) {
        return new ClassedIterator<>(elementClass);
    }

    /**
     * Get the value of the primitive associated with this key, or throw.
     *
     * @param key            The key of the primitive.
     * @param primitiveClass The class the primitive should be of.
     * @param <P>            The type the primitive should be of.
     * @return The value of the primitive.
     * @throws ElementException If the primitive was not found, or the value did not conform to P.
     */
    public <P> P get(String key, Class<P> primitiveClass) throws ElementException {
        return getPrimitive(key).requireValue(primitiveClass);
    }

    /**
     * Get the primitive element associated with this key.
     *
     * @param key The key associated with the primitive.
     * @return The found DataPrimitive.
     * @throws ElementException If the element was not found, or the element was not a primitive.
     */
    public DataPrimitive getPrimitive(String key) throws ElementException {
        return get(key).requireOf(DataPrimitive.class);
    }

    /**
     * Get the element associated with this key.
     *
     * @param key The key of the element.
     * @return The element associated with this key, or a new {@link DataNull}.
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
        if (Objects.equals(keyCache, key)) return elementCache;
        if (value != null){
            keyCache = key;
            return elementCache = value.get(key);
        }
        return null;
    }

    /**
     * Get the array element associated with this key.
     *
     * @param key The key associated with the array.
     * @return The found DataArray.
     * @throws ElementException If the element was not found, or the element was not a DataArray.
     */
    public DataArray getArray(String key) throws ElementException {
        return get(key).requireOf(DataArray.class);
    }

    /**
     * Get the boolean value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The boolean value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a boolean.
     */
    public boolean getBoolean(String key) throws ElementException {
        return getPrimitive(key).booleanValue();
    }

    /**
     * Get the boolean value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a boolean.
     * @return The boolean value of the primitive, or the default value.
     */
    public boolean getBoolean(String key, boolean def) {
        return get(key, Boolean.class, def);
    }

    /**
     * Get the value of the primitive associated with this key, or a default.
     *
     * @param key            The key of the primitive.
     * @param primitiveClass The class the primitive should be of.
     * @param <P>            The type the primitive should be of.
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

    /**
     * Get the double value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The double value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    public double getDouble(String key) throws ElementException {
        return getPrimitive(key).doubleValue();
    }

    /**
     * Get the double value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a number.
     * @return The double value of the primitive, or the default value.
     */
    public double getDouble(String key, double def) {
        return get(key, Number.class, def).doubleValue();
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

    /**
     * Get the value of the first {@link DataPrimitive} associated with one of these keys.
     *
     * @param keys           The possible keys of the primitive.
     * @param primitiveClass The class the primitive should be of.
     * @param <P>            The type the primitive should be of.
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
     * Get the float value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The float value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    public float getFloat(String key) throws ElementException {
        return getPrimitive(key).floatValue();
    }

    /**
     * Get the float value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a number.
     * @return The float value of the primitive, or the default value.
     */
    public float getFloat(String key, float def) {
        return get(key, Number.class, def).floatValue();
    }

    /**
     * Get the int value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The int value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    public int getInt(String key) throws ElementException {
        return getPrimitive(key).intValue();
    }

    /**
     * Get the int value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a number.
     * @return The int value of the primitive, or the default value.
     */
    public int getInt(String key, int def) {
        return get(key, Number.class, def).intValue();
    }

    /**
     * Get the list element associated with this key.
     *
     * @param key The key associated with the list.
     * @return The found DataList.
     * @throws ElementException If the element was not found, or the element was not a DataList.
     */
    public DataList getList(String key) throws ElementException {
        return get(key).requireOf(DataList.class);
    }

    /**
     * Get the long value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The long value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    public long getLong(String key) throws ElementException {
        return getPrimitive(key).longValue();
    }

    /**
     * Get the long value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a number.
     * @return The long value of the primitive, or the default value.
     */
    public long getLong(String key, long def) {
        return get(key, Number.class, def).intValue();
    }

    /**
     * Get the map element associated with this key.
     *
     * @param key The key associated with the map.
     * @return The found DataMap.
     * @throws ElementException If the element was not found, or the element was not a DataMap.
     */
    public DataMap getMap(String key) throws ElementException {
        return get(key).requireOf(DataMap.class);
    }

    /**
     * Get the String value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The String value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a String.
     */
    public String getString(String key) throws ElementException {
        return getPrimitive(key).stringValue();
    }

    /**
     * Get the String value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a String.
     * @return The String value of the primitive, or the default value.
     */
    public String getString(String key, String def) {
        return get(key, String.class, def);
    }

    /**
     * Get the value of the primitive associated with this key, cast to P.
     *
     * @param key The key associated with the primitive element.
     * @param <P> The type to cast to.
     * @return The cast value.
     * @throws ElementException If the element was not found, or is not a primitive.
     */
    public <P> P getUnsafe(String key) throws ElementException {
        return getPrimitive(key).valueUnsafe();
    }

    /**
     * Get the value of the primitive associated with this key, cast to P, or a default value.
     * This does not return the default value if the cast throws!
     *
     * @param key The key associated with the primitive element.
     * @param <P> The type to cast to.
     * @param def The default value to return if the element is not a primitive.
     * @return The cast value.
     */
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
     * Runs the action if the specified element is present and is a DataArray.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifArray(String key, Consumer<DataArray> action) {
        ifArray(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataArray, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataArray.
     */
    public void ifArray(String key, Consumer<DataArray> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isArray()) action.accept(el.asArray());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present and is a DataList.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifList(String key, Consumer<DataList> action) {
        ifList(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataList, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataList.
     */
    public void ifList(String key, Consumer<DataList> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isList()) action.accept(el.asList());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present and is a DataMap.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifMap(String key, Consumer<DataMap> action) {
        ifMap(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataMap, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataMap.
     */
    public void ifMap(String key, Consumer<DataMap> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isMap()) action.accept(el.asMap());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present.
     * This will never be a DataNull.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifPresent(String key, Consumer<DataElement> action) {
        ifPresent(key, action, null);
    }

    /**
     * Runs the action if the specified element is present, or the elseAction if not.
     * This will never be a DataNull.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present.
     */
    public void ifPresent(String key, Consumer<DataElement> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null) action.accept(el);
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifPrimitive(String key, Consumer<DataPrimitive> action) {
        ifPrimitive(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataPrimitive.
     */
    public void ifPrimitive(String key, Consumer<DataPrimitive> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isPrimitive()) action.accept(el.asPrimitive());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive and it's value is of the specified type.
     *
     * @param key            The key to find the element at.
     * @param primitiveClass The class the DataPrimitive should be of.
     * @param <P>            The type the DataPrimitive should be of.
     * @param action         The consumer to run when the element is found.
     */
    public <P> void ifPrimitiveOf(String key, Class<P> primitiveClass, Consumer<P> action) {
        ifPrimitiveOf(key, primitiveClass, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive and it's value is of the specified type, or the elseAction if not.
     *
     * @param key            The key to find the element at.
     * @param primitiveClass The class the DataPrimitive should be of.
     * @param <P>            The type the DataPrimitive should be of.
     * @param elseAction     The runnable to run when the element is not present or is not a DataPrimitive, or it's value is not of the specified type.
     * @param action         The consumer to run when the element is found.
     */
    public <P> void ifPrimitiveOf(String key, Class<P> primitiveClass, Consumer<P> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isPrimitive()) {
            final DataPrimitive primitive = el.asPrimitive();
            if (primitive.valueOf(primitiveClass)) {
                action.accept(primitive.valueUnsafe());
                return;
            }
        }
        if (elseAction != null) elseAction.run();
    }

    /**
     * Navigate this map with the specified keys until the element is reached, or the next element is not a map.
     *
     * @param keys The keys to navigate the elements with.
     * @return The found DataElement, or {@code DataNull} if it was not found.
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
     * @param <P>            The type the DataPrimitives values should be of.
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
     * @return This DataMap.
     * @throws ElementException When this key isn't present.
     */
    public DataMap requireKey(String key) throws ElementException {
        if (!hasKey(key)) throw ElementException.requireField(this, key);
        return this;
    }

    /**
     * @param key The key to check for.
     * @return True if the key is contained in this DataMap.
     */
    public boolean hasKey(String key) {
        if (Objects.equals(key, keyCache)) return true;
        if (value == null) return false;
        return value.containsKey(key);
    }

    /**
     * Check if all these keys are present, or else throw.
     *
     * @param keys The keys to check for.
     * @return This DataMap.
     * @throws ElementException When these keys aren't present.
     */
    public DataMap requireKeys(String... keys) throws ElementException {
        return requireKeys(Arrays.asList(keys));
    }

    /**
     * Check if all these keys are present, or else throw.
     *
     * @param keys The keys to check for.
     * @return This DataMap.
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
     * @return This DataMap.
     * @throws ElementException When the element associated with this key is not of the required type.
     */
    public DataMap requireOf(String key, Class<? extends DataElement> clazz) throws ElementException {
        final DataElement el = getOrNull(key);
        if (!(clazz.isInstance(el))) throw ElementException.requireType(el, clazz);
        return this;
    }

    public DataArray valueArray(){
        return new DataArray(value.values().toArray(new DataElement[0]));
    }

    public DataList valueList(){
        return new DataList(new ArrayList<>(value.values()));
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
    public DataMap name(String name) throws IllegalArgumentException {
        super.name(name);
        return this;
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
