package io.github.thegatesdev.maple;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

/**
 * An element that maps String keys to DataElement values.
 */
public class DataMap extends DataElement implements MappedElements<String> {

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
     * @return The previous element mapped to this key, or null if it wasn't present.
     */
    public DataElement set(String key, DataElement element) {
        var old = elements.put(key, element);
        connectThis(element, key);
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Removes the mapping for this key.
     * Also see {@link Map#remove(Object)}.
     *
     * @param key The key of the mapping to be removed.
     * @return The previous element mapped to this key, or null if it wasn't present.
     */
    public DataElement remove(String key) {
        var old = elements.remove(key);
        if (old != null) old.disconnect();
        return old;
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
        if (el == null) return connectThis(new DataNull(), key);
        return el;
    }


    // -- ELEMENT

    private DataElement connectThis(DataElement element, String key) {
        return element.connect(this, key);
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public DataMap asMap() throws UnsupportedOperationException {
        return this;
    }

    @Override
    public void ifMap(Consumer<DataMap> mapConsumer, Runnable elseAction) {
        mapConsumer.accept(this);
    }

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
