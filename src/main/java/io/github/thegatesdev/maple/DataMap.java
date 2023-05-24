package io.github.thegatesdev.maple;

import java.util.Collections;
import java.util.Map;

public class DataMap extends DataElement {

    private final Map<String, DataElement> elements, view;

    DataMap(Map<String, DataElement> map) {
        elements = map;
        view = Collections.unmodifiableMap(elements);
    }

    // -- MAP

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
