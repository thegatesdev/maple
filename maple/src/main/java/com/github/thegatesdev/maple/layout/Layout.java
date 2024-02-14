package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.exception.LayoutParseException;

/**
 * Defines the layout expected of some element, and parses it to type T.
 *
 * @param <T> the type to parse to
 */
@FunctionalInterface
public interface Layout<T> {

    /**
     * Get a new builder for constructing dictionary layouts.
     *
     * @return the new builder
     */
    static DictLayout.Builder dictionary() {
        return new DictLayout.Builder();
    }

    /**
     * Parse the given value according to this layout.
     *
     * @param value the element to parse
     * @return the value conforming to this layout
     */
    T parse(Element value) throws LayoutParseException;
}
