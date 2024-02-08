package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryDictElement;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * An element representing a dictionary of string keys mapped to element values.
 */
public sealed interface DictElement extends Element, ElementCollection permits MemoryDictElement {


    /**
     * Get the element mapped to the supplied key.
     *
     * @param key the key for the element
     * @return the element at the key
     * @throws com.github.thegatesdev.maple.exception.ElementKeyNotPresentException if the key was not present
     */
    Element get(String key);

    /**
     * Find the element mapped to the supplied key.
     *
     * @param key the key for the element
     * @return an optional containing the element at the key if it was present
     */
    Optional<Element> find(String key);

    /**
     * Iterate the key-value mappings in this dictionary element using the given action.
     *
     * @param action the action to run for each mapping
     */
    void each(BiConsumer<String, Element> action);

    /**
     * Move the keys and values in this dictionary element to a new map.
     *
     * @return A modifiable map containing the mappings from this dictionary element
     */
    Map<String, Element> copyBack();


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
}
