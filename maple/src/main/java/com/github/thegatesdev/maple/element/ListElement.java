package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryListElement;

import java.util.List;
import java.util.Optional;

/**
 * An element representing a list of values.
 */
public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

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
}
