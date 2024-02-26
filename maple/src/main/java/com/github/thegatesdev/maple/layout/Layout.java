package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.exception.LayoutParseException;

/**
 * Defines and enforces a layout on some element.
 *
 * @param <E> the resulting type
 */
public interface Layout<E extends Element> {

    /**
     * Get a new builder for constructing dictionary layouts.
     *
     * @return the new builder
     */
    static DictLayout.Builder dictionary() {
        return new DictLayout.Builder();
    }

    /**
     * Get a layout on a plain element that returns the same element.
     *
     * @return the identity layout
     */
    static Layout<? extends Element> identity() {
        return value -> value;
    }

    /**
     * Parse the given value according to this layout.
     *
     * @param value the element to parse
     * @return the element conforming to this layout
     * @throws LayoutParseException if the layout failed to parse for this element
     */
    E parse(Element value) throws LayoutParseException;
}
