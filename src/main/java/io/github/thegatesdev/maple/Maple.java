package io.github.thegatesdev.maple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;

/**
 * Utility class for constructing and operating on Maple elements.
 */
public class Maple {

    static final IntFunction<Map<String, DataElement>> DEFAULT_MAP_IMPL = HashMap::new;
    static final IntFunction<List<DataElement>> DEFAULT_LIST_IMPL = ArrayList::new;


    // -- CONSTRUCT

    // MAP

    /**
     * @return A new DataMap using the default Map implementation.
     */
    public static DataMap map() {
        return map(DEFAULT_MAP_IMPL.apply(0));
    }

    /**
     * @param map A map implementation to construct the DataMap with.
     * @return A new DataMap using the supplied map.
     */
    public static DataMap map(Map<String, DataElement> map) {
        if (!map.isEmpty()) throw new IllegalArgumentException("The supplied map must be empty");
        return new DataMap(map);
    }

    // LIST

    /**
     * @return A new DataList using the default List implementation.
     */
    public static DataList list() {
        return list(DEFAULT_LIST_IMPL.apply(0));
    }

    /**
     * @param list A list implementation to construct the DataMap with.
     * @return A new DataMap using the supplied map.
     */
    public static DataList list(List<DataElement> list) {
        if (!list.isEmpty()) throw new IllegalArgumentException("The supplied map must be empty");
        return new DataList(list);
    }
}
