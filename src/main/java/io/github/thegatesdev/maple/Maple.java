package io.github.thegatesdev.maple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Maple {

    private static final Supplier<Map<String, DataElement>> DEFAULT_MAP_IMPL = HashMap::new;
    private static final Supplier<List<DataElement>> DEFAULT_LIST_IMPL = ArrayList::new;


    // -- CONSTRUCT

    public static DataMap map() {
        return map(DEFAULT_MAP_IMPL);
    }

    public static DataMap map(Supplier<Map<String, DataElement>> mapSupplier) {
        return map(mapSupplier.get());
    }

    public static DataMap map(Map<String, DataElement> inputMap) {
        return new DataMap(inputMap);
    }
}
