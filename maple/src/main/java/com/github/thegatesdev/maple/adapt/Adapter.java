package com.github.thegatesdev.maple.adapt;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.exception.AdaptException;

/**
 * An adapter tries to convert output from a specific parser to a Maple structure.
 *
 * @author Timar Karels
 */
public interface Adapter {
    /**
     * Adapt the given Object value to an element,
     * according to the output type specification.
     *
     * @param input the value to adapt
     * @return the element representing the value
     * @throws AdaptException if the value type does not match any type defined in the specification of the output type
     */
    Element adapt(Object input);
}
