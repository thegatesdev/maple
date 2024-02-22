package com.github.thegatesdev.maple.adapt;

import com.github.thegatesdev.maple.adapt.impl.SnakeYamlEngineAdapter;
import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ListElement;

import java.util.Collection;
import java.util.Map;

/**
 * An adapter tries to convert output from a specific parser to a Maple structure.
 */
public interface Adapter {

    static Adapter get(OutputType type) {
        return SnakeYamlEngineAdapter.INSTANCE;
    }


    /**
     * Adapt the given map of object keys and values to a new dictionary element,
     * according to the output type specification.
     * The keys are converted to strings by calling {@link Object#toString()}.
     *
     * @param entries the entries to adapt
     * @return the dictionary element with the adapted entries
     * @throws IllegalArgumentException if any of the values could not be adapted
     */
    DictElement adapt(Map<?, ?> entries) throws IllegalArgumentException;

    /**
     * Adapt the given collection of objects to a new list element,
     * according to the output type specification.
     *
     * @param values the values to adapt
     * @return the list element with the adapted values
     * @throws IllegalArgumentException if any of the values could not be adapted
     */
    ListElement adapt(Collection<?> values) throws IllegalArgumentException;

    /**
     * Adapt the given Object value to an element,
     * according to the output type specification.
     *
     * @param value the value to adapt
     * @return the element representing the value
     * @throws IllegalArgumentException if the value type does not match any type defined in the specification of the output type
     */
    Element adapt(Object value) throws IllegalArgumentException;

    /**
     * Get the output type of this adapter.
     *
     * @return the output type
     */
    OutputType outputType();
}
