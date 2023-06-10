package io.github.thegatesdev.maple;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
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
 * An element that maps String keys to DataElement values.
 */
public class DataMap extends DataElement implements MappedElements<String> {

    private final Map<String, DataElement> elements, view;
    private DataList cachedElementsList;

    private String prevKey;
    private DataElement prevVal;

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
        requireNotLocked();
        checkPrev(key);
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
        requireNotLocked();
        checkPrev(key);
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
        if (Objects.equals(prevKey, key)) return prevVal;
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

    private void checkPrev(String key) {
        if (key.equals(prevKey)) {
            prevKey = null;
            prevVal = null;
        }
    }

    /**
     * @return The number of mappings in this map.
     */
    public int size() {
        return elements.size();
    }

    // -- LIST

    private DataList buildList() {
        DataList list = Maple.list(size());
        elements.values().forEach(list::add);
        return list;
    }

    public DataList valueList() {
        if (cachedElementsList == null) {
            cachedElementsList = buildList();
            cachedElementsList.lockContent();
        }
        return cachedElementsList;
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
    public DataMap shallowCopy() {
        return new DataMap(elements);
    }

    @Override
    public DataMap deepCopy() {
        var copy = new DataMap(Maple.DEFAULT_MAP_IMPL.apply(elements.size()));
        elements.forEach((s, element) -> copy.set(s, element.deepCopy()));
        return copy;
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

    @Override
    public String toString() {
        if (elements.isEmpty()) return "emptyMap";
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("map{");
        int len = elements.size();
        for (Map.Entry<String, DataElement> entry : elements.entrySet()) {
            stringBuilder
                    .append("'").append(entry.getKey()).append("'")
                    .append(": ").append(entry.getValue());
            if (--len > 0) stringBuilder.append(", ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

}
