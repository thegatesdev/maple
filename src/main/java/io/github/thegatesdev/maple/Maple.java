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

    /**
     * @return A new DataMap using the default map implementation.
     */
    public static DataMap map() {
        return map(DEFAULT_MAP_IMPL);
    }

    /**
     * @param mapSupplier A supplier to construct the DataMap with.
     * @return A new DataMap using the supplied map.
     */
    public static DataMap map(Supplier<Map<String, DataElement>> mapSupplier) {
        return map(mapSupplier.get());
    }

    /**
     * @param inputMap A map to construct the DataMap with.
     * @return A new DataMap using the supplied map.
     */
    public static DataMap map(Map<String, DataElement> inputMap) {
        return new DataMap(inputMap);
    }
}
