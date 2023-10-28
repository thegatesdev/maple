package io.github.thegatesdev.maple.conversion;

import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.element.DataMap;

import java.util.List;
import java.util.Map;

/**
 * Provides methods to convert {@code Map} and {@code List} instances to their element equivalent.
 */
public interface Conversion {

    /**
     * Convert the given {@code Map} to a new {@code DataMap}.
     *
     * @param someMap the {@code Map} to convert
     * @return a new {@code DataMap} holding the convertible entries
     */
    DataMap convertMap(Map<?, ?> someMap);

    /**
     * Convert the given {@code List} to a new {@code DataList}.
     *
     * @param someList the {@code List} to convert
     * @return a new {@code DataList} holding the convertible values
     */
    DataList convertList(List<?> someList);
}
