/*
Copyright 2024 Timar Karels

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

package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.internal.MemoryDictElement;
import com.github.thegatesdev.maple.exception.ElementKeyNotPresentException;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * An element representing a dictionary of string keys mapped to element values.
 *
 * @author Timar Karels
 * @see ElementType#DICTIONARY
 */
public sealed interface DictElement extends Element, ElementCollection permits MemoryDictElement {

    /**
     * Get a dictionary element containing the keys and values from the given map.
     *
     * @param values the values for the dictionary
     * @return the dictionary element containing the values
     * @throws NullPointerException if the given map is null
     */
    static DictElement of(Map<String, Element> values) {
        return MemoryDictElement.of(values);
    }

    /**
     * Get a new builder for dictionary elements.
     *
     * @return the new builder
     */
    static Builder builder() {
        return MemoryDictElement.builder();
    }

    /**
     * Get a new builder for dictionary elements.
     *
     * @param initialCapacity the initial capacity of the dictionary
     * @return the new builder
     * @throws IllegalArgumentException if the initial capacity is negative
     */
    static Builder builder(int initialCapacity) {
        return MemoryDictElement.builder(initialCapacity);
    }

    /**
     * Get a dictionary element containing no values.
     *
     * @return the empty dictionary element
     */
    static DictElement empty() {
        return MemoryDictElement.EMPTY;
    }


    /**
     * Get the element at the given key.
     *
     * @param key the key for the element
     * @return the element at the key
     * @throws NullPointerException          if the given key is null
     * @throws ElementKeyNotPresentException if the key is not present
     */
    Element get(String key);

    /**
     * Find the element at the given key.
     *
     * @param key the key for the element
     * @return an optional containing the element at the key if it is present
     * @throws NullPointerException if the given key is null
     */
    Optional<Element> find(String key);

    /**
     * Iterate the entries in this dictionary element using the given action.
     *
     * @param action the action to run for each mapping
     * @throws NullPointerException if the given action is null
     */
    void each(BiConsumer<String, Element> action);

    /**
     * Get an unmodifiable view of the entries in this dictionary.
     *
     * @return the view of the dictionary elements
     */
    Map<String, Element> view();

    /**
     * Get a dictionary builder containing the entries in this dictionary.
     *
     * @return the new builder
     */
    Builder toBuilder();


    @Override
    default boolean isDict() {
        return true;
    }

    @Override
    default DictElement getDict() {
        return this;
    }

    @Override
    default ElementType type() {
        return ElementType.DICTIONARY;
    }


    /**
     * A builder for dictionary elements.
     */
    sealed interface Builder permits MemoryDictElement.Builder {

        /**
         * Create a new dictionary element with the entries in this builder.
         *
         * @return the new dictionary element
         */
        DictElement build();


        /**
         * Put the given element at the given key, optionally replacing the value already present.
         *
         * @param key     the key to put the element at
         * @param element the element to put
         * @return this builder
         * @throws NullPointerException if the given key or element is null
         */
        Builder put(String key, Element element);

        /**
         * Put all the entries from the given dictionary element, optionally replacing the values already present.
         *
         * @param values the dictionary to merge
         * @return this builder
         * @throws NullPointerException if the given dictionary element is null
         */
        Builder putAll(DictElement values);

        /**
         * Put all the entries from the given map, optionally replacing the values already present.
         *
         * @param values the map to merge
         * @return this builder
         * @throws NullPointerException if the given map is null
         */
        Builder putAll(Map<String, Element> values);

        /**
         * Remove the entry at the given key if present.
         *
         * @param key the key for the entry to remove
         * @return this builder
         * @throws NullPointerException if the given key is null
         */
        Builder remove(String key);

        /**
         * Get an unmodifiable view of the entries in this builder.
         *
         * @return the view of the dictionary entries
         */
        Map<String, Element> view();
    }
}
