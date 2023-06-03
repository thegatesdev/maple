package io.github.thegatesdev.maple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Utility class for constructing and operating on Maple elements.
 */
public class Maple {

    private static final Supplier<Map<String, DataElement>> DEFAULT_MAP_IMPL = HashMap::new;
    private static final Supplier<List<DataElement>> DEFAULT_LIST_IMPL = ArrayList::new;


    // -- CONSTRUCT

    // MAP

    /**
     * @return A new DataMap using the default Map implementation.
     */
    public static DataMap map() {
        return map(DEFAULT_MAP_IMPL);
    }

    /**
     * @param mapSupplier A supplier to construct the DataMap with.
     * @return A new DataMap using the supplied map.
     */
    public static DataMap map(Supplier<Map<String, DataElement>> mapSupplier) {
        var input = mapSupplier.get();
        if (!input.isEmpty()) throw new IllegalArgumentException("The supplied map must be empty");
        return new DataMap(input);
    }

    // LIST

    /**
     * @return A new DataList using the default List implementation.
     */
    public static DataList list() {
        return list(DEFAULT_LIST_IMPL);
    }

    /**
     * @param listSupplier A supplier to construct the DataMap with.
     * @return A new DataMap using the supplied map.
     */
    public static DataList list(Supplier<List<DataElement>> listSupplier) {
        var input = listSupplier.get();
        if (!input.isEmpty()) throw new IllegalArgumentException("The supplied map must be empty");
        return new DataList(input);
    }
}
