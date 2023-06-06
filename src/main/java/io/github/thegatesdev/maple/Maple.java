package io.github.thegatesdev.maple;

import java.util.*;
import java.util.function.IntFunction;
import java.util.function.Supplier;

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

    // VALUE

    /**
     * @param value The value to be contained in the element.
     * @return A new DataValue containing the specified object.
     */
    public static DataValue value(Object value) {
        return new DataValue.Static(Objects.requireNonNull(value));
    }

    /**
     * @param type          The type of the value to be supplied.
     * @param valueSupplier The supplier for the value.
     * @return A new DataValue producing values of the specified type using the specified supplier.
     */
    public static <T> DataValue value(Class<T> type, Supplier<T> valueSupplier) {
        return new DataValue.Dynamic<>(Objects.requireNonNull(type), Objects.requireNonNull(valueSupplier));
    }

    // NULL

    /**
     * @return A new DataNull.
     */
    public static DataNull nothing() {
        return new DataNull();
    }
}
