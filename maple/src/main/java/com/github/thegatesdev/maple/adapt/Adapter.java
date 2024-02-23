package com.github.thegatesdev.maple.adapt;

import com.github.thegatesdev.maple.element.Element;

/**
 * An adapter tries to convert output from a specific parser to a Maple structure.
 */
public interface Adapter {
    /**
     * Adapt the given Object value to an element,
     * according to the output type specification.
     *
     * @param value the value to adapt
     * @return the element representing the value
     * @throws IllegalArgumentException if the value type does not match any type defined in the specification of the output type
     */
    Element adapt(Object value) throws IllegalArgumentException;
}
