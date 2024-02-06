package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryListElement;

import java.util.List;
import java.util.Optional;

/**
 * An element representing a list of values.
 */
public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

    /**
     * Get the element at the supplied index.
     *
     * @param index the index for the element
     * @return the element at the index
     * @throws IndexOutOfBoundsException when the index is out of bounds
     */
    Element get(int index);

    /**
     * Find the element at the supplied index.
     *
     * @param index the index for the element
     * @return an optional containing the element at the index if it was present
     */
    Optional<Element> find(int index);

    /**
     * Move the values of this list element to a new list.
     *
     * @return A modifiable list containing the values from this list element
     */
    List<Element> copyBack();


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
