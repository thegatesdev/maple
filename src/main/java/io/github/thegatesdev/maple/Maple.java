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

    // -- READ

    /**
     * Read this object as a DataElement, so that when;
     * <ul>
     * <li>{@code input == null} -> {@link DataNull#DataNull()}.
     * <li>{@code input instanceof Object[]} -> {@link Maple#readList(Object...)}.
     * <li>{@code input instanceof List<?>} -> {@link Maple#readList(List)}.
     * <li>{@code input instanceof Iterable<?>} -> {@link Maple#readList(Iterable)}.
     * <li>{@code input instanceof Map<?,?>} -> {@link Maple#readMap(Map)}.
     * <li>If none of the above apply -> {@link Maple#value(Object)}.
     * </ul>
     * If the specified object is already a DataElement, it will be returned as is.
     *
     * @param input The Object to read from.
     * @return The new element.
     */
    public static DataElement read(Object input) {
        if (input == null) return nothing();
        if (input instanceof DataElement element) return element;

        if (input instanceof Object[] objects) return readList(objects);
        if (input instanceof List<?> list) return readList(list);
        if (input instanceof Iterable<?> iterable) return readList(iterable);

        if (input instanceof Map<?, ?> map) return readMap(map);

        return value(input);
    }

    public static DataList readList(Object... objects) {
        final DataList output = list(objects.length);
        for (int i = 0; i < objects.length; i++)
            output.set(i, Maple.read(objects[i]));
        return output;
    }

    public static DataList readList(List<?> list) {
        final DataList output = list(list.size());
        for (int i = 0; i < list.size(); i++)
            output.set(i, Maple.read(list.get(i)));
        return output;
    }

    public static DataList readList(Iterable<?> iterable) {
        final DataList output = list(2);
        for (Object o : iterable) output.add(read(o));
        return output;
    }

    public static DataMap readMap(Map<?, ?> map) {
        final DataMap output = map(map.size());
        map.forEach((key, val) -> {
            if (key instanceof String sKey) output.set(sKey, read(val));
        });
        return output;
    }

    // -- CONSTRUCT

    // MAP

    /**
     * @return A new DataMap using the default Map implementation.
     */
    public static DataMap map() {
        return map(0);
    }

    /**
     * @param initialCapacity The initial amount of items the map can hold.
     * @return A new DataMap using the default Map implementation.
     */
    public static DataMap map(int initialCapacity) {
        return map(DEFAULT_MAP_IMPL.apply(initialCapacity));
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
        return list(0);
    }

    /**
     * @param initialCapacity The initial amount of items the list can hold.
     * @return A new DataList using the default List implementation.
     */
    public static DataList list(int initialCapacity) {
        return list(DEFAULT_LIST_IMPL.apply(initialCapacity));
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
