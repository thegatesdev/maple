package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.exception.ElementException;

import java.util.Optional;

public interface Layout<E extends Element> {

    /**
     * Get a builder for creating dictionary layouts.
     *
     * @return the new builder
     */
    static DictLayout.Builder dictionary() {
        return new DictLayout.Builder();
    }

    /**
     * Apply the layout on the given element, optionally returning a new element.
     * Example of applying a layout:
     * <pre>{@code
     * Element applied = layout.apply(element).orElse(element);
     * }</pre>
     *
     * @param element the element to apply to
     * @return optionally a new element
     * @throws ElementException if the layout failed to apply to the element
     */
    Optional<E> apply(Element element);
}
