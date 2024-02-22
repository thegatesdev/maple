package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryListElement;

import java.util.List;
import java.util.Optional;

/**
 * An element representing a list of values.
 */
public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

    /**
     * Get a new builder for list elements.
     *
     * @return the new builder
     */
    static Builder builder() {
        return MemoryListElement.builder();
    }

    /**
     * Get a new builder for list elements.
     *
     * @param initialCapacity the initial capacity of the list
     * @return the new builder
     */
    static Builder builder(int initialCapacity) {
        return MemoryListElement.builder(initialCapacity);
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
     * @throws IndexOutOfBoundsException when the index is out of bounds
     */
    Element get(int index);

    /**
     * Find the element at the given index.
     *
     * @param index the index for the element
     * @return an optional containing the element at the index if it was present
     */
    Optional<Element> find(int index);

    /**
     * Get an unmodifiable view of the values in this list.
     *
     * @return the view of the list elements
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
     * A builder for list elements.
     */
    sealed interface Builder permits MemoryListElement.Builder {

        /**
         * Create a new list element with the values in this builder.
         *
         * @return the new list element
         */
        ListElement build();


        /**
         * Add the given element to the end of this list.
         *
         * @param element the element to add
         * @return this builder
         */
        Builder add(Element element);

        /**
         * Add the elements in the given list element to the end of this list.
         *
         * @param element the list element to add the values from
         * @return this builder
         */
        Builder addAll(ListElement element);

        /**
         * Add the elements in the given list to the end of this list.
         *
         * @param elements the elements to add
         * @return this builder
         */
        Builder addAll(List<Element> elements);

        /**
         * Add the elements in the given array to the end of this list.
         *
         * @param elements the elements to add
         * @return this builder
         */
        Builder addAll(Element[] elements);

        /**
         * Remove the element at the given index from this list.
         *
         * @param index the index for the element to remove
         * @return this builder
         */
        Builder remove(int index);

        /**
         * Remove the given element from the list.
         *
         * @param element the element to remove
         * @return this builder
         */
        Builder remove(Element element);

        /**
         * Get an unmodifiable view of the values in this builder.
         *
         * @return the view of the list values
         */
        List<Element> view();
    }
}
