package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.element.impl.internal.*;
import io.github.thegatesdev.maple.exception.*;

import java.math.*;
import java.util.*;
import java.util.function.*;

/**
 * An element representing a dictionary of string keys mapped to element values.
 *
 * @author Timar Karels
 * @see ElementType#DICT
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
     * Get a new builder for creating dictionary elements.
     *
     * @return the new builder
     */
    static Builder builder() {
        return MemoryDictElement.builder();
    }

    /**
     * Get a new builder for creating dictionary elements.
     *
     * @param initialCapacity the initial capacity of the dictionary
     * @return the new builder
     * @throws IllegalArgumentException if the initial capacity is negative
     */
    static Builder builder(int initialCapacity) {
        return MemoryDictElement.builder(initialCapacity);
    }

    /**
     * Build a new dictionary element by applying the given action to a new builder.
     *
     * @param action the builder action
     * @return the built element
     */
    static DictElement build(Consumer<DictElement.Builder> action) {
        DictElement.Builder builder = builder();
        action.accept(builder);
        return builder.build();
    }

    /**
     * Build a new dictionary element by applying the given action to a new builder.
     *
     * @param action          the builder action
     * @param initialCapacity the initial capacity of the dictionary
     * @return the built element
     */
    static DictElement build(int initialCapacity, Consumer<DictElement.Builder> action) {
        DictElement.Builder builder = builder(initialCapacity);
        action.accept(builder);
        return builder.build();
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
     * @return an optional containing the element if it is present
     * @throws NullPointerException if the given key is null
     */
    Optional<Element> find(String key);

    /**
     * Perform the given action for each entry in this dictionary.
     *
     * @param action the action to perform
     * @throws NullPointerException if the given action is null
     */
    void each(BiConsumer<String, Element> action);

    /**
     * Get an unmodifiable view of the entries in this dictionary.
     *
     * @return the view of the entries
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
        return ElementType.DICT;
    }


    /**
     * Indicates that the values in this dict element are the same as in the given dict element.
     *
     * @param other the dict element to compare
     * @return {@code true} if the contents match
     */
    boolean contentEquals(DictElement other);

    /**
     * The {@code DictElement} implemetation of {@code equals}, on top of the default semantics,
     * guarantees that if the result is {@code true}, this dict element can replace the given dict element.
     * <p>
     * The contents of two dict elements being equal does not imply replacability.
     * An example of this would be when the given dict element is backed by a file,
     * while this one is stored in memory, they cannot be interchanged.
     * To check if the contents are equal, use the {@link #contentEquals(DictElement)} method.
     * </p>
     *
     * @see Object#equals(Object)
     */
    boolean equals(Object other);


    /**
     * A builder for creating dictionary elements.
     * <p>
     * The builder is NOT guaranteed to be thread safe.
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
         * Put the given value at the given key, optionally replacing the value already present.
         *
         * @param key   the key to put the value at
         * @param value the value to put
         * @return this builder
         * @throws NullPointerException if the given key or value is null
         */
        default Builder put(String key, String value) {
            return put(key, Element.of(value));
        }

        /**
         * Put the given value at the given key, optionally replacing the value already present.
         *
         * @param key   the key to put the value at
         * @param value the value to put
         * @return this builder
         * @throws NullPointerException if the given key is null
         */
        default Builder put(String key, boolean value) {
            return put(key, Element.of(value));
        }

        /**
         * Put the given value at the given key, optionally replacing the value already present.
         *
         * @param key   the key to put the value at
         * @param value the value to put
         * @return this builder
         * @throws NullPointerException if the given key is null
         */
        default Builder put(String key, int value) {
            return put(key, Element.of(value));
        }

        /**
         * Put the given value at the given key, optionally replacing the value already present.
         *
         * @param key   the key to put the value at
         * @param value the value to put
         * @return this builder
         * @throws NullPointerException if the given key is null
         */
        default Builder put(String key, long value) {
            return put(key, Element.of(value));
        }

        /**
         * Put the given value at the given key, optionally replacing the value already present.
         *
         * @param key   the key to put the value at
         * @param value the value to put
         * @return this builder
         * @throws NullPointerException if the given key is null
         */
        default Builder put(String key, float value) {
            return put(key, Element.of(value));
        }

        /**
         * Put the given value at the given key, optionally replacing the value already present.
         *
         * @param key   the key to put the value at
         * @param value the value to put
         * @return this builder
         * @throws NullPointerException if the given key is null
         */
        default Builder put(String key, double value) {
            return put(key, Element.of(value));
        }

        /**
         * Put the given value at the given key, optionally replacing the value already present.
         *
         * @param key   the key to put the value at
         * @param value the value to put
         * @return this builder
         * @throws NullPointerException if the given key is null
         */
        default Builder put(String key, BigInteger value) {
            return put(key, Element.of(value));
        }

        /**
         * Put the given value at the given key, optionally replacing the value already present.
         *
         * @param key   the key to put the value at
         * @param value the value to put
         * @return this builder
         * @throws NullPointerException if the given key is null
         */
        default Builder put(String key, BigDecimal value) {
            return put(key, Element.of(value));
        }

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
         * Remove the entries at the given keys if present.
         * Ignores 'null' keys.
         *
         * @param keys the keys for the entries to remove
         * @return this builder
         * @throws NullPointerException if the given array is null
         */
        Builder remove(String... keys);

        /**
         * Remove the entries at the given keys if present.
         * Ignores 'null' keys.
         *
         * @param keys the keys for the entries to remove
         * @return this builder
         * @throws NullPointerException if the given array is null
         */
        Builder remove(Collection<String> keys);

        /**
         * Keep only the entries at the given keys.
         * Ignores 'null' keys.
         *
         * @param keys the keys for the entries to keep
         * @return this builder
         */
        Builder keep(String... keys);

        /**
         * Keep only the entries at the given keys.
         * Ignores 'null' keys.
         *
         * @param keys the keys for the entries to keep
         * @return this builder
         */
        Builder keep(Collection<String> keys);

        /**
         * Get an unmodifiable view of the entries in this builder.
         *
         * @return the view of the entries
         */
        Map<String, Element> view();
    }
}

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
