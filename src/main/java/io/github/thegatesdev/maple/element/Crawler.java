package io.github.thegatesdev.maple.element;

import java.util.Optional;

/**
 * Processes the contents of an element, including any descendants.
 */
@FunctionalInterface
public interface Crawler {

    /**
     * Process a found element.
     * Returns the element replacement, or an empty {@code Optional} to keep the original.
     *
     * @param element the element that was found
     * @return the optional replacement
     */
    Optional<DataElement> process(DataElement element);
}
