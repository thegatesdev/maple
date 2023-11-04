/*
Copyright 2023 Timar Karels

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.ElementType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * An element mapping {@code String} keys to {@code DataValue} values.
 */
public final class DataMap implements DataElement, DataDictionary<String> {

    private final Map<String, DataElement> elements;

    private DataMap(Map<String, DataElement> elements) {
        this.elements = elements;
    }


    /**
     * Create a new map element builder.
     *
     * @return the new builder
     */
    public static Builder builder(){
        return new Builder();
    }

    /**
     * Create a new map element builder with the given initial capacity.
     *
     * @param initialCapacity the initial capacity of the builder
     * @return the new builder
     */
    public static Builder builder(int initialCapacity){
        return new Builder(initialCapacity);
    }

    // Operations

    @Override
    public DataElement getOrNull(String key) {
        return elements.get(Objects.requireNonNull(key, "key cannot be null"));
    }

    @Override
    public void each(Consumer<DataElement> elementConsumer) {
        elements.values().forEach(elementConsumer);
    }

    @Override
    public DataElement crawl(Crawler crawler) {
        var builder = builder(size());
        elements.forEach((key, value) ->
                builder.add(key, crawler.process(value.crawl(crawler))));
        return builder.build();
    }

    // Information

    @Override
    public int size() {
        return elements.size();
    }

    // Value

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    // Type

    @Override
    public ElementType getType() {
        return ElementType.MAP;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public DataMap asMap() {
        return this;
    }


    /**
     * A builder for {@link DataMap}.
     */
    public static class Builder{
        private final Map<String, DataElement> buildingElements;


        private Builder(){
            this(5);
        }

        private Builder(int initialCapacity){
            buildingElements = new LinkedHashMap<>(initialCapacity);
        }


        /**
         * Build the elements in the builder to a new map element.
         *
         * @return the new map element
         */
        public DataMap build(){
            return new DataMap(new LinkedHashMap<>(buildingElements));
        }


        /**
         * Add an element in the builder at the given key, potentially overwriting existing values.
         *
         * @param key the key for the element
         * @param element the element to add
         */
        public Builder add(String key, DataElement element){
            buildingElements.put(key, element);
            return this;
        }

        /**
         * Add all elements from the given map to the builder.
         *
         * @param elements the elements to add
         */
        public Builder addFrom(Map<String, DataElement> elements){
            buildingElements.putAll(elements);
            return this;
        }

        /**
         * Add all elements from the given map element to the builder.
         *
         * @param dataMap the map to add the elements from
         */
        public Builder addFrom(DataMap dataMap){
            buildingElements.putAll(dataMap.elements);
            return this;
        }
    }
}
