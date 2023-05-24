package io.github.thegatesdev.maple;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Maple {

    private static final Supplier<Map<String, DataElement>> DEFAULT_MAP_IMPL = HashMap::new;

    // -- CONSTRUCT

    public static DataMap map(Supplier<Map<String, DataElement>> mapSupplier) {
        return new DataMap(mapSupplier.get());
    }

    public static DataMap map() {
        return map(DEFAULT_MAP_IMPL);
    }
}
