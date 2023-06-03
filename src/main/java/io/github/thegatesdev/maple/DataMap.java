package io.github.thegatesdev.maple;

import io.github.thegatesdev.maple.exception.ElementException;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public class DataMap extends DataElement {

    private final Map<String, DataElement> elements, view;

    DataMap(Map<String, DataElement> map) {
        elements = map;
        view = Collections.unmodifiableMap(elements);
    }

    // -- BASIC OPERATIONS

    /**
     * Associates the key with the element in the map, optionally replacing the old value.
     * Also see {@link Map#put(Object, Object)}.
     *
     * @param key     The key to associate the element with.
     * @param element The element to associate the key with.
     */
    public void set(String key, DataElement element) {
        element.connect(this, key);
        var old = elements.put(key, element);
        if (old != null) old.disconnect();
    }

    /**
     * Removes the mapping for this key.
     * Also see {@link Map#remove(Object)}.
     *
     * @param key The key of the mapping to be removed.
     */
    public void remove(String key) {
        var old = elements.remove(key);
        if (old != null) old.disconnect();
    }

    /**
     * Get the element associated with this key, or null.
     *
     * @param key The key of the element.
     * @return The element associated with this key, or {@code null}.
     */
    public DataElement getOrNull(String key) {
        return elements.get(key);
    }

    /**
     * Get the element associated with this key.
     *
     * @param key The key of the element.
     * @return The element associated with this key, or a new {@link DataNull}.
     */
    public DataElement get(String key) {
        var el = getOrNull(key);
        if (el == null) return new DataNull().connect(this, key);
        return el;
    }

    // -- ELEMENT GETTERS


    // VALUE

    /**
     * Get the value element associated with this key.
     *
     * @param key The key associated with the value element.
     * @return The found DataValue.
     * @throws ElementException If the element was not found, or the element was not a value element.
     */
    public DataValue getValue(String key) throws ElementException {
        return get(key).requireOf(DataValue.class);
    }

    /**
     * Get the value element associated with this key, or a default.
     *
     * @param key The key associated with the value element.
     * @param def The value to return if the element is not a value element, or is not present.
     * @return The found DataValue, or the default value.
     */
    public DataValue getValue(String key, DataValue def) {
        return get(key, DataValue.class, def);
    }

    /**
     * Runs the action if the specified element is present and is a DataValue.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifValue(String key, Consumer<DataValue> action) {
        ifValue(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataValue, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataValue.
     */
    public void ifValue(String key, Consumer<DataValue> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isValue()) action.accept(el.asValue());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Get the value of the value element associated with this key, cast to P.
     *
     * @param key The key associated with the value element.
     * @param <P> The type to cast to.
     * @return The cast value.
     * @throws ElementException If the element was not found, or is not a value element.
     */
    public <P> P getUnsafe(String key) throws ElementException {
        return getValue(key).valueUnsafe();
    }

    /**
     * Get the value of the value element associated with this key, cast to P, or a default value.
     * This does not return the default value if the cast throws!
     *
     * @param key The key associated with the primitive element.
     * @param <P> The type to cast to.
     * @param def The default value to return if the element is not a value element or is not present.
     * @return The cast value.
     */
    public <P> P getUnsafe(String key, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isValue()) return def;
        final P val = el.asValue().valueUnsafe();
        return def == null ? val : (val == null ? def : val);
    }

    /**
     * Get the value of the value element associated with this key, or throw.
     *
     * @param key            The key of the value element.
     * @param primitiveClass The class the value should be of.
     * @param <P>            The type the value should be of.
     * @return The value of the value element.
     * @throws ElementException If the value element was not found, or the value did not conform to P.
     */
    public <P> P get(String key, Class<P> primitiveClass) throws ElementException {
        return getValue(key).valueOrThrow(primitiveClass);
    }

    /**
     * Get the value of the value element associated with this key, or a default.
     *
     * @param key            The key of the value element.
     * @param primitiveClass The class the value should be of.
     * @param <P>            The type the value should be of.
     * @param def            The default value to return when the element is not present, not a value element, or the type does not match.
     * @return The value of the value element, or the default value.
     */
    public <P> P get(String key, Class<P> primitiveClass, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isValue()) return def;
        final P val = el.asValue().valueOrNull(primitiveClass);
        return def == null ? val : (val == null ? def : val);
    }


    // MAP

    /**
     * Get the map element associated with this key.
     *
     * @param key The key associated with the map element.
     * @return The found DataMap.
     * @throws ElementException If the element was not found, or the element was not a map element.
     */
    public DataMap getMap(String key) throws ElementException {
        return get(key).requireOf(DataMap.class);
    }

    /**
     * Get the map element associated with this key, or a default.
     *
     * @param key The key associated with the value element.
     * @param def The value to return if the element is not a map element, or is not present.
     * @return The found DataMap, or the default value.
     */
    public DataMap getMap(String key, DataMap def) {
        return get(key, DataMap.class, def);
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

    // -- ELEMENT

    @Override
    protected Map<String, DataElement> raw() {
        return elements;
    }

    @Override
    public Map<String, DataElement> view() {
        return view;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
