package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.element.impl.internal.*;

import java.math.*;
import java.util.*;
import java.util.function.*;

/**
 * An element representing a list of values.
 *
 * @author Timar Karels
 * @see ElementType#LIST
 */
public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

    /**
     * Get a list element containing the values from the given array.
     *
     * @param values the values for the list
     * @return the list element containing the values
     * @throws NullPointerException if the given array is null
     */
    static ListElement of(Element... values) {
        return MemoryListElement.of(values);
    }

    /**
     * Get a list element containing the values from the given collection.
     *
     * @param values the values for the list
     * @return the list element containing the values
     * @throws NullPointerException if the given collection is null
     */
    static ListElement of(Collection<Element> values) {
        return MemoryListElement.of(values);
    }

    /**
     * Get a new builder for creating list elements.
     *
     * @return the new builder
     */
    static Builder builder() {
        return MemoryListElement.builder();
    }

    /**
     * Get a new builder for creating list elements.
     *
     * @param initialCapacity the initial capacity of the list
     * @return the new builder
     * @throws IllegalArgumentException if the initial capacity is negative
     */
    static Builder builder(int initialCapacity) {
        return MemoryListElement.builder(initialCapacity);
    }

    /**
     * Build a new list element by applying the given action to a new builder.
     *
     * @param action the builder action
     * @return the built element
     */
    static ListElement build(Consumer<ListElement.Builder> action) {
        ListElement.Builder builder = builder();
        action.accept(builder);
        return builder.build();
    }

    /**
     * Build a new list element by applying the given action to a new builder.
     *
     * @param action          the builder action
     * @param initialCapacity the initial capacity of the list
     * @return the built element
     */
    static ListElement build(int initialCapacity, Consumer<ListElement.Builder> action) {
        ListElement.Builder builder = builder(initialCapacity);
        action.accept(builder);
        return builder.build();
    }

    /**
     * Get a list element containing no values.
     *
     * @return the empty list element
     */
    static ListElement empty() {
        return MemoryListElement.EMPTY;
    }


    /**
     * Get the element at the given index.
     *
     * @param index the index for the element
     * @return the element at the index
     * @throws IndexOutOfBoundsException if the index is out of bounds for this list
     */
    Element get(int index);

    /**
     * Find the element at the given index.
     * Invalid indexes will return an empty optional.
     *
     * @param index the index for the element
     * @return an optional containing the element at the index if it is present
     */
    Optional<Element> find(int index);

    /**
     * Get an unmodifiable view of the values in this list.
     *
     * @return the view of the values
     */
    List<Element> view();

    /**
     * Get a list builder containing the values in this list.
     *
     * @return the new builder
     */
    Builder toBuilder();

    /**
     * Move the values in this list element to a new array.
     * Any changes to the array are not reflected in this list.
     *
     * @return An array containing the values from this list element.
     */
    Element[] toArray();


    @Override
    default boolean isList() {
        return true;
    }

    @Override
    default ListElement getList() {
        return this;
    }

    @Override
    default ElementType type() {
        return ElementType.LIST;
    }


    /**
     * Indicates that the values in this list element are the same as in the given list element.
     *
     * @param other the list element to compare
     * @return {@code true} if the contents match
     */
    boolean contentEquals(ListElement other);

    /**
     * The {@code ListElement} implemetation of {@code equals}, on top of the default semantics,
     * guarantees that if the result is {@code true}, this list element can replace the given list element.
     * <p>
     * The contents of two list elements being equal does not imply replacability.
     * An example of this would be when the given list element is backed by a file,
     * while this one is stored in memory, they cannot be interchanged.
     * To check if the contents are equal, use the {@link #contentEquals(ListElement)} method.
     * </p>
     *
     * @see Object#equals(Object)
     */
    boolean equals(Object other);


    /**
     * A builder for creating list elements.
     * <p>
     * The builder is NOT guaranteed to be thread safe.
     */
    sealed interface Builder permits MemoryListElement.Builder {

        /**
         * Get a list element containing the values in this builder.
         *
         * @return the list element
         */
        ListElement build();


        /**
         * Add the given element to the end of this list.
         *
         * @param element the element to add
         * @return this builder
         * @throws NullPointerException if the given element is null
         */
        Builder add(Element element);

        /**
         * Add the given value to the end of this list.
         *
         * @param value the value to add
         * @return this builder
         * @throws NullPointerException if the given value is null
         */
        default Builder add(String value) {
            return add(Element.of(value));
        }

        /**
         * Add the given value to the end of this list.
         *
         * @param value the value to add
         * @return this builder
         */
        default Builder add(boolean value) {
            return add(Element.of(value));
        }

        /**
         * Add the given value to the end of this list.
         *
         * @param value the value to add
         * @return this builder
         */
        default Builder add(int value) {
            return add(Element.of(value));
        }

        /**
         * Add the given value to the end of this list.
         *
         * @param value the value to add
         * @return this builder
         */
        default Builder add(long value) {
            return add(Element.of(value));
        }

        /**
         * Add the given value to the end of this list.
         *
         * @param value the value to add
         * @return this builder
         */
        default Builder add(float value) {
            return add(Element.of(value));
        }

        /**
         * Add the given value to the end of this list.
         *
         * @param value the value to add
         * @return this builder
         */
        default Builder add(double value) {
            return add(Element.of(value));
        }

        /**
         * Add the given value to the end of this list.
         *
         * @param value the value to add
         * @return this builder
         */
        default Builder add(BigInteger value) {
            return add(Element.of(value));
        }

        /**
         * Add the given value to the end of this list.
         *
         * @param value the value to add
         * @return this builder
         */
        default Builder add(BigDecimal value) {
            return add(Element.of(value));
        }

        /**
         * Add the elements in the given list element to the end of this list.
         *
         * @param element the list element to add the values from
         * @return this builder
         * @throws NullPointerException if the given list element is null
         */
        Builder addAll(ListElement element);

        /**
         * Add the elements in the given list to the end of this list.
         *
         * @param elements the elements to add
         * @return this builder
         * @throws NullPointerException if the given list is null
         */
        Builder addAll(List<Element> elements);

        /**
         * Add the elements in the given array to the end of this list.
         *
         * @param elements the elements to add
         * @return this builder
         * @throws NullPointerException if the given array is null
         */
        Builder addAll(Element... elements);

        /**
         * Remove the element at the given index from this list.
         *
         * @param index the index for the element to remove
         * @return this builder
         * @throws IndexOutOfBoundsException if the index is out of bounds
         */
        Builder remove(int index);

        /**
         * Remove the given element from the list.
         *
         * @param element the element to remove
         * @return this builder
         * @throws NullPointerException if the given element is null
         */
        Builder remove(Element element);

        /**
         * Get an unmodifiable view of the values in this builder.
         *
         * @return the view of the values
         */
        List<Element> view();
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
