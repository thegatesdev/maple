package io.github.thegatesdev.maple;

import io.github.thegatesdev.maple.data.*;

import java.util.List;
import java.util.Map;

/*
Copyright (C) 2022  Timar Karels

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

/**
 * Utility class for constructing and operating on Maple elements.
 */
public class Maple {
    // -- READ

    /**
     * Read this object as a DataElement, so that when;
     * <ul>
     * <li>{@code input == null} -> {@link DataNull}.
     * <li>{@code input instanceof Object[]} -> {@link Maple#readList(Object...)}.
     * <li>{@code input instanceof List<?>} -> {@link Maple#readList(List)}.
     * <li>{@code input instanceof Iterable<?>} -> {@link Maple#readList(Iterable)}.
     * <li>{@code input instanceof Map<?,?>} -> {@link Maple#readMap(Map)}.
     * <li>If none of the above apply -> {@link DataValue}.
     * </ul>
     * If the specified object is already a DataElement, it will be returned as is.
     *
     * @param input The Object to read from.
     * @return The new element.
     */
    public static DataElement read(Object input) {
        if (input == null) return new DataNull();
        if (input instanceof DataElement element) return element;

        if (input instanceof Object[] objects) return readList(objects);
        if (input instanceof List<?> list) return readList(list);

        // Some Maps are Iterable, but we still want a map.
        if (input instanceof Map<?, ?> map) return readMap(map);

        if (input instanceof Iterable<?> iterable) return readList(iterable);

        return DataValue.of(input);
    }

    /**
     * Read this varargs array to a DataList.
     *
     * @param objects The input array.
     * @return A new DataList containing the values of the input array read using {@link Maple#read(Object)}
     */
    public static DataList readList(Object... objects) {
        final DataList output = new DataList(objects.length);
        for (int i = 0; i < objects.length; i++)
            output.add(Maple.read(objects[i]));
        return output;
    }

    /**
     * Read this list to a DataList.
     *
     * @param list The input list.
     * @return A new DataList containing the values of the input list read using {@link Maple#read(Object)}
     */
    public static DataList readList(List<?> list) {
        final DataList output = new DataList(list.size());
        for (int i = 0; i < list.size(); i++)
            output.add(Maple.read(list.get(i)));
        return output;
    }

    /**
     * Read this iterable to a DataList.
     *
     * @param iterable The input iterable.
     * @return A new DataList containing the values of the input iterable read using {@link Maple#read(Object)}
     */
    public static DataList readList(Iterable<?> iterable) {
        final DataList output = new DataList();
        for (Object o : iterable) output.add(read(o));
        return output;
    }

    /**
     * Read this map to a DataMap.
     *
     * @param map The input map.
     * @return A new DataMap containing all the mappings where the key is a {@code String}, the values read using {@link Maple#read(Object)}
     */
    public static DataMap readMap(Map<?, ?> map) {
        final DataMap output = new DataMap(map.size());
        map.forEach((key, val) -> {
            if (key instanceof String sKey) output.set(sKey, read(val));
        });
        return output;
    }
}
