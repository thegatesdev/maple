package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.Maple;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

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

    private final Map<String, DataElement> elements;

    private DataMap(Map<String, DataElement> elements) {
        this.elements = elements;
    }

    public DataMap() {
        this(new LinkedHashMap<>());
    }

    public DataMap(int initialCapacity) {
        this(new LinkedHashMap<>(initialCapacity));
    }

    // -- OPERATIONS

    /**
     * Set the element in the map at the supplied key, optionally replacing the previous value.
     *
     * @return The previous value at this key
     */
    public DataElement set(String key, DataElement element) {
        var old = elements.put(key, element.connect(this, key));
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Set the element read from the supplied input in the map at the supplied key, optionally replacing the previous value.
     *
     * @return The previous value at this key
     */
    public DataElement set(String key, Object input) {
        return set(key, Maple.read(input));
    }

    /**
     * Remove the element at the supplied key.
     *
     * @return The removed value
     */
    public DataElement remove(String key) {
        var old = elements.remove(key);
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Clear the map of all containing elements.
     *
     * @return The amount of elements cleared, or the size before it was cleared
     */
    public int clear() {
        int size = size();
        elements.values().forEach(DataElement::disconnect);
        elements.clear();
        return size;
    }

    // -- GET

    /**
     * Get the element at the supplied key, or {@code null} if it does not exist.
     */
    public DataElement getOrNull(String key) {
        return elements.get(key);
    }

    /**
     * Get the element at the supplied key, or a new DataNull if it does not exist.
     */
    public DataElement get(String key) {
        var el = getOrNull(key);
        return el == null ? new DataNull().connect(this, key) : el;
    }

    /**
     * Get the size of this map, or how many elements it contains
     */
    public int size() {
        return elements.size();
    }

    /**
     * Run the supplied consumer for every value in this map.
     */
    public void each(Consumer<DataElement> elementConsumer) {
        elements.values().forEach(elementConsumer);
    }

    @Override
    public void crawl(Consumer<DataElement> consumer) {
        each(element -> {
            consumer.accept(element);
            element.crawl(consumer);
        });
    }

    @Override
    public void crawl(Function<DataElement, DataElement> function) {
        for (var entry : elements.entrySet()) {
            var original = entry.getValue();
            DataElement replacement = function.apply(original);
            if (replacement != null) entry.setValue(replacement);
            else original.crawl(function);
        }
    }

    // -- SELF

    @Override
    public Map<String, DataElement> view() {
        return Collections.unmodifiableMap(elements);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public DataElement copy() {
        var copy = new DataMap(elements.size());
        elements.forEach((s, element) -> copy.set(s, element.copy()));
        return copy;
    }

    @Override
    public String toString() {
        if (elements.isEmpty()) return "emptyMap";
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("map{");
        int len = elements.size();
        for (Map.Entry<String, DataElement> entry : elements.entrySet()) {
            stringBuilder
                    .append(entry.getKey()).append(": ").append(entry.getValue());
            if (--len > 0) stringBuilder.append(", ");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    /**
     * @return {@code true}
     */
    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public DataMap asMap() throws UnsupportedOperationException {
        return this;
    }
}
