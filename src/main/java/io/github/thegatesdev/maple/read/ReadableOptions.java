package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.Maple;
import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.data.DataMap;
import io.github.thegatesdev.maple.exception.ElementException;
import io.github.thegatesdev.maple.read.struct.DataType;
import io.github.thegatesdev.maple.read.struct.DataTypeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

public class ReadableOptions {

    private final Object MUT_ENTRIES = new Object(), MUT_AFTER = new Object();
    protected List<OptionEntry<?>> entries;
    protected List<AfterEntry> afterFunctions;

    // -- ACTIONS

    public DataMap read(DataMap data) {
        // Create output
        final DataMap output = Maple.map();
        try {
            if (entries != null) synchronized (MUT_ENTRIES) {
                for (OptionEntry<?> entry : entries) {
                    final DataElement read = readEntry(entry, data.getOrNull(entry.key));
                    if (read == null) throw ElementException.requireField(data, entry.key);
                    output.set(entry.key, read);
                }
            }
            // afterFunctions allow for some calculations ( e.g. generate a predicate for multiple conditions )
            if (afterFunctions != null) synchronized (MUT_AFTER) {
                afterFunctions.forEach((value) -> output.set(value.key, value.modifier.apply(output)));
            }
        } catch (ElementException e) {
            throw e;
        } catch (Exception e) {
            throw new ElementException(data, "readableData error; %s".formatted(e.getMessage()), e);
        }
        return output;
    }

    private static StringBuilder displayEntry(OptionEntry<?> entry) {
        final StringBuilder builder = new StringBuilder("A " + entry.dataType().id() + "; ");
        if (entry.hasDefault) {
            if (entry.defaultValue == null) builder.append("optional");
            else builder.append("default value: ").append(entry.defaultValue);
        } else builder.append("required");
        return builder;
    }

    private static <E extends DataElement> E readEntry(OptionEntry<E> value, DataElement element) {
        if (element != null) return value.dataType.read(element); // Present
        // Not present
        if (value.hasDefault) return value.defaultValue;
        return null; // Not present and no default is error
    }

    // -- MUTATE

    public ReadableOptions add(String key, DataTypeHolder<?> holder) {
        return add(new OptionEntry<>(key, holder.dataType()));
    }

    public <E extends DataElement> ReadableOptions add(String key, DataTypeHolder<E> holder, E def) {
        return add(new OptionEntry<>(key, holder.dataType(), def));
    }

    public <E extends DataElement> ReadableOptions add(DataTypeHolder<E> holder, Map<String, E> def) {
        def.forEach((s, t) -> this.add(s, holder, t));
        return this;
    }

    public <E extends DataElement> ReadableOptions add(List<String> values, DataTypeHolder<E> holder, E def) {
        values.forEach(s -> add(s, holder, def));
        return this;
    }

    public ReadableOptions add(List<String> values, DataTypeHolder<?> holder) {
        values.forEach(s -> this.add(s, holder));
        return this;
    }

    protected ReadableOptions add(OptionEntry<?> entry) {
        if (entries == null) entries = new ArrayList<>();
        entries.add(entry);
        return this;
    }

    public ReadableOptions after(String s, Function<DataMap, DataElement> function) {
        if (afterFunctions == null) afterFunctions = new ArrayList<>();
        afterFunctions.add(new AfterEntry(s, function));
        return this;
    }

    // -- GET/SET

    public String displayEntries() {
        final StringBuilder builder = new StringBuilder();
        for (final OptionEntry<?> entry : entries)
            builder.append(entry.key).append(": ").append(displayEntry(entry)).append("\n");
        return builder.toString();
    }

    // -- CLASS

    private record OptionEntry<E extends DataElement>(String key, DataType<E> dataType, E defaultValue,
                                                      boolean hasDefault) implements DataTypeHolder<E> {
        public OptionEntry(String key, DataType<E> dataType, E defaultValue) {
            this(key, dataType, defaultValue, true);
        }

        public OptionEntry(String key, DataType<E> dataType) {
            this(key, dataType, null, false);
        }
    }

    private record AfterEntry(String key, Function<DataMap, DataElement> modifier) {
    }
}
