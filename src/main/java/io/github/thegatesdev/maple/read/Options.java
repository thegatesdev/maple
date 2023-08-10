package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.data.DataMap;
import io.github.thegatesdev.maple.data.DataValue;
import io.github.thegatesdev.maple.exception.ElementException;
import io.github.thegatesdev.maple.read.struct.DataType;
import io.github.thegatesdev.maple.read.struct.DataTypeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
 * Represents a set of options that a dataMap must or may optionally fulfill.
 */
public class Options {

    private final List<Option<?>> entries = new ArrayList<>(1);


    private static Object readEntry(DataMap input, DataElement el, Option<?> entry) {
        if (el != null) return entry.dataType.read(el); // Present
        if (!entry.hasDefault) throw ElementException.requireField(input, entry.key); // Not present and no default! Error!
        return entry.defaultValue; // We have a default! Phew..
    }


    /**
     * Read the supplied input map to the supplied output map using the supplied options.
     *
     * @return The supplied output map with the applied options
     */
    public static DataMap read(Options options, DataMap input, DataMap output) {
        try {
            for (var entry : options.entries) output.set(entry.key, readEntry(input, input.getOrNull(entry.key), entry));
        } catch (ElementException e) {
            throw e;
        } catch (Exception e) {
            throw new ElementException(input, "Error while reading options: " + e.getMessage(), e);
        }
        return output;
    }

    /**
     * Read the supplied input map to a new map using the supplied options.
     *
     * @return The new map with the applied options
     */
    public static DataMap read(Options options, DataMap input) {
        return read(options, input, new DataMap(input.size()));
    }


    /**
     * Add a required option of the supplied dataType at the supplied key.
     * Results in an elementException being thrown when not present.
     */
    public Options add(String key, DataTypeHolder<?> holder) {
        return add(new Option<>(key, holder.dataType()));
    }

    /**
     * Add an optional option of the supplied dataType, at the supplied key.
     * Results in a dataNull when not present.
     */
    public Options optional(String key, DataTypeHolder<?> holder) {
        return add(new Option<>(key, holder.dataType(), null));
    }

    /**
     * Add an optional option at the supplied key of the supplied dataType.
     * When not present, the supplied default will be used.
     */
    public <Type> Options add(String key, DataTypeHolder<Type> holder, Type def) {
        return add(new Option<>(key, holder.dataType(), Objects.requireNonNull(def)));
    }

    /**
     * Add an optional option at the supplied key of the supplied dataType.
     * When not present, the supplied default dataValue value will be used.
     * This is a convenience method to avoid having to call {@link DataValue#of(Object)}.
     */
    public <Val> Options addVal(String key, DataTypeHolder<DataValue<Val>> holder, Val def) {
        return add(key, holder.dataType(), DataValue.of(def));
    }


    private Options add(Option<?> entry) {
        entries.add(entry);
        return this;
    }


    private record Option<Type>(String key,
                                DataType<Type> dataType,
                                Type defaultValue,
                                boolean hasDefault) {
        private Option {
            Objects.requireNonNull(key);
            Objects.requireNonNull(dataType);
        }

        private Option(String key, DataType<Type> dataType, Type defaultValue) {
            this(key, dataType, defaultValue, true);
        }

        private Option(String key, DataType<Type> dataType) {
            this(key, dataType, null, false);
        }
    }
}
