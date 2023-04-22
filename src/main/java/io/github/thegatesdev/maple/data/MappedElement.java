package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.exception.ElementException;

import java.util.*;
import java.util.function.Consumer;

/**
 * An abstract class for mapped elements. Implemented by DataMap.
 * Has common methods for getting different elements using a string key.
 */
public interface MappedElement extends Iterable<DataElement> {

    <E extends DataElement> Iterator<Map.Entry<String, E>> iterator(Class<E> elementClass);

    /**
     * Get the value of the primitive associated with this key, or throw.
     *
     * @param key            The key of the primitive.
     * @param primitiveClass The class the primitive should be of.
     * @param <P>            The type the primitive should be of.
     * @return The value of the primitive.
     * @throws ElementException If the primitive was not found, or the value did not conform to P.
     */
    default <P> P get(String key, Class<P> primitiveClass) throws ElementException {
        return getPrimitive(key).requireValue(primitiveClass);
    }

    /**
     * Get the primitive element associated with this key.
     *
     * @param key The key associated with the primitive.
     * @return The found DataPrimitive.
     * @throws ElementException If the element was not found, or the element was not a primitive.
     */
    default DataPrimitive getPrimitive(String key) throws ElementException {
        return get(key).requireOf(DataPrimitive.class);
    }

    /**
     * Get the primitive element associated with this key, or a default.
     *
     * @param key The key associated with the primitive.
     * @param def The value to return if the element is not a primitive.
     * @return The found DataPrimitive, or the default value.
     */
    default DataPrimitive getPrimitive(String key, DataPrimitive def) {
        return get(key, DataPrimitive.class, def);
    }

    /**
     * Get the element associated with this key.
     *
     * @param key The key of the element.
     * @return The element associated with this key, or a new {@link DataNull}.
     */
    default DataElement get(String key) {
        final DataElement el = getOrNull(key);
        return el == null ? new DataNull().setData(self(), key) : el;
    }

    DataElement getOrNull(String key);

    /**
     * Get the array element associated with this key.
     *
     * @param key The key associated with the array.
     * @return The found DataArray.
     * @throws ElementException If the element was not found, or the element was not a DataArray.
     */
    default DataArray getArray(String key) throws ElementException {
        return get(key).requireOf(DataArray.class);
    }

    /**
     * Get the array element associated with this key, or a default.
     *
     * @param key The key associated with the array.
     * @param def The value to return if the element is not a array.
     * @return The found DataArray, or the default value.
     */
    default DataArray getArray(String key, DataArray def) {
        return get(key, DataArray.class, def);
    }

    /**
     * Get the boolean value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The boolean value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a boolean.
     */
    default boolean getBoolean(String key) throws ElementException {
        return getPrimitive(key).booleanValue();
    }

    /**
     * Get the boolean value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a boolean.
     * @return The boolean value of the primitive, or the default value.
     */
    default boolean getBoolean(String key, boolean def) {
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
    default <P> P get(String key, Class<P> primitiveClass, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isPrimitive()) return def;
        if (def == null) // Shortcut, def is already null, so returning null wouldn't matter.
            return el.asPrimitive().valueOrNull(primitiveClass);
        final P val = el.asPrimitive().valueOrNull(primitiveClass);
        return val == null ? def : val;
    }

    /**
     * Get the element associated with this key, or a default.
     *
     * @param key          The key of the element.
     * @param elementClass The class the element should be of.
     * @param def          The default value to return when the element is not present, or the type does not match.
     * @param <D>          The type the element should be of.
     * @return The element, or the default value.
     */
    default <D extends DataElement> D get(String key, Class<D> elementClass, D def) {
        final DataElement el = getOrNull(key);
        return el == null || !el.isOf(elementClass) ? def : el.unsafeCast();
    }

    /**
     * Get the double value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The double value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    default double getDouble(String key) throws ElementException {
        return getPrimitive(key).doubleValue();
    }

    /**
     * Get the double value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a number.
     * @return The double value of the primitive, or the default value.
     */
    default double getDouble(String key, double def) {
        return get(key, Number.class, def).doubleValue();
    }

    /**
     * Find the first element associated with one of the keys.
     *
     * @param keys The possible keys of the element.
     * @return The element associated with one of these keys, or a new {@link DataNull};
     */
    default DataElement getFirst(String... keys) {
        for (String key : keys) {
            final DataElement el = getOrNull(key);
            if (el != null) return el;
        }
        return new DataNull().setData(self(), null);
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
    default <P> P getFirst(Class<P> primitiveClass, String... keys) {
        for (String key : keys) {
            final DataElement el = getOrNull(key);
            if (el != null && el.isPrimitive()) return el.asPrimitive().requireValue(primitiveClass);
        }
        throw ElementException.requireField(self(), String.join(" or ", keys));
    }

    /**
     * Get the float value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The float value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    default float getFloat(String key) throws ElementException {
        return getPrimitive(key).floatValue();
    }

    /**
     * Get the float value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a number.
     * @return The float value of the primitive, or the default value.
     */
    default float getFloat(String key, float def) {
        return get(key, Number.class, def).floatValue();
    }

    /**
     * Get the int value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The int value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    default int getInt(String key) throws ElementException {
        return getPrimitive(key).intValue();
    }

    /**
     * Get the int value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a number.
     * @return The int value of the primitive, or the default value.
     */
    default int getInt(String key, int def) {
        return get(key, Number.class, def).intValue();
    }

    /**
     * Get the list element associated with this key.
     *
     * @param key The key associated with the list.
     * @return The found DataList.
     * @throws ElementException If the element was not found, or the element was not a DataList.
     */
    default DataList getList(String key) throws ElementException {
        return get(key).requireOf(DataList.class);
    }

    /**
     * Get the list element associated with this key, or a default.
     *
     * @param key The key associated with the list.
     * @param def The value to return if the element is not a list.
     * @return The found DataList, or the default value.
     */
    default DataList getList(String key, DataList def) {
        return get(key, DataList.class, def);
    }

    /**
     * Get the long value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The long value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    default long getLong(String key) throws ElementException {
        return getPrimitive(key).longValue();
    }

    /**
     * Get the long value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a number.
     * @return The long value of the primitive, or the default value.
     */
    default long getLong(String key, long def) {
        return get(key, Number.class, def).intValue();
    }

    /**
     * Get the map element associated with this key.
     *
     * @param key The key associated with the map.
     * @return The found DataMap.
     * @throws ElementException If the element was not found, or the element was not a DataMap.
     */
    default DataMap getMap(String key) throws ElementException {
        return get(key).requireOf(DataMap.class);
    }

    /**
     * Get the map element associated with this key, or a default.
     *
     * @param key The key associated with the map.
     * @param def The value to return if the element is not a map.
     * @return The found DataMap, or the default value.
     */
    default DataMap getMap(String key, DataMap def) {
        return get(key, DataMap.class, def);
    }

    /**
     * Get the String value from the primitive associated with this key.
     *
     * @param key The key associated with the element.
     * @return The String value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a String.
     */
    default String getString(String key) throws ElementException {
        return getPrimitive(key).stringValue();
    }

    /**
     * Get the String value from the primitive associated with this key, or a default.
     *
     * @param key The key associated with the element.
     * @param def The value to return if the element is not a primitive, or the value is not a String.
     * @return The String value of the primitive, or the default value.
     */
    default String getString(String key, String def) {
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
    default <P> P getUnsafe(String key) throws ElementException {
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
    default <P> P getUnsafe(String key, P def) {
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
    default boolean hasKeys(String... keys) {
        return hasKeys(Arrays.asList(keys));
    }

    boolean hasKeys(Collection<String> keys);

    /**
     * Runs the action if the specified element is present and is a DataArray.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    default void ifArray(String key, Consumer<DataArray> action) {
        ifArray(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataArray, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataArray.
     */
    default void ifArray(String key, Consumer<DataArray> action, Runnable elseAction) {
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
    default void ifList(String key, Consumer<DataList> action) {
        ifList(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataList, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataList.
     */
    default void ifList(String key, Consumer<DataList> action, Runnable elseAction) {
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
    default void ifMap(String key, Consumer<DataMap> action) {
        ifMap(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataMap, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataMap.
     */
    default void ifMap(String key, Consumer<DataMap> action, Runnable elseAction) {
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
    default void ifPresent(String key, Consumer<DataElement> action) {
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
    default void ifPresent(String key, Consumer<DataElement> action, Runnable elseAction) {
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
    default void ifPrimitive(String key, Consumer<DataPrimitive> action) {
        ifPrimitive(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataPrimitive.
     */
    default void ifPrimitive(String key, Consumer<DataPrimitive> action, Runnable elseAction) {
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
    default <P> void ifPrimitiveOf(String key, Class<P> primitiveClass, Consumer<P> action) {
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
    default <P> void ifPrimitiveOf(String key, Class<P> primitiveClass, Consumer<P> action, Runnable elseAction) {
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
    default DataElement navigate(String... keys) {
        return navigate(0, keys);
    }

    default DataElement navigate(int current, String[] keys) {
        final String key = keys[current];
        final DataElement element = get(key);
        if (current == keys.length - 1) return element;
        if (!element.isMap()) return new DataNull().setData(self(), key);
        return element.asMap().navigate(++current, keys);
    }

    /**
     * @param primitiveClass The class the DataPrimitives values should be of.
     * @param <P>            The type the DataPrimitives values should be of.
     * @return A new Map containing the key values pairs of the DataPrimitives in this DataMap conforming to {@code elementClass}.
     */
    default <P> Map<String, P> primitiveMap(Class<P> primitiveClass) {
        final Map<String, P> out = new LinkedHashMap<>(size());
        iterator(DataPrimitive.class).forEachRemaining(e -> {
            final P val = e.getValue().valueOrNull(primitiveClass);
            if (val != null) out.put(e.getKey(), val);
        });
        return out;
    }

    int size();

    /**
     * Check if all these keys are present, or else throw.
     *
     * @param keys The keys to check for.
     * @throws ElementException When these keys aren't present.
     */
    default void requireKeys(String... keys) throws ElementException {
        requireKeys(Arrays.asList(keys));
    }

    /**
     * Check if all these keys are present, or else throw.
     *
     * @param keys The keys to check for.
     * @throws ElementException When these keys aren't present.
     */
    default void requireKeys(Collection<String> keys) throws ElementException {
        if (!hasKeys(keys)) throw ElementException.requireField(self(), String.join(" and ", keys));
    }

    /**
     * Check if the element associated with this key is an instance of {@code clazz}
     *
     * @param key   The key associated with the element.
     * @param clazz The class the element should be of.
     * @throws ElementException When the element associated with this key is not of the required type.
     */
    default void requireOf(String key, Class<? extends DataElement> clazz) throws ElementException {
        final DataElement el = getOrNull(key);
        if (!(clazz.isInstance(el))) throw ElementException.requireType(el, clazz);
    }

    DataArray valueArray();

    DataList valueList();

    DataElement self();
}
