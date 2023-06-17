package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.Maple;
import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.data.DataList;
import io.github.thegatesdev.maple.data.DataMap;
import io.github.thegatesdev.maple.data.DataValue;
import io.github.thegatesdev.maple.exception.ElementException;
import io.github.thegatesdev.maple.read.struct.DataType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
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

public class Readable implements DataType {
    private static final Map<Class<?>, Readable> SINGLE_TYPE_CACHE = new HashMap<>();
    private static final Map<DataType, Readable> LIST_TYPE_CACHE = new HashMap<>();

    private final Function<DataElement, DataElement> readFunction;
    private final String identifier;
    private DataTypeInfo info;

    public Readable(String identifier, final Function<DataElement, DataElement> readFunction) {
        this.identifier = identifier;
        this.readFunction = readFunction;
    }

    // ELEMENTS

    public static Readable list(String identifier, Function<DataList, DataElement> readFunction) {
        return new Readable(identifier, el -> readFunction.apply(el.requireOf(DataList.class)));
    }

    public static Readable value(String identifier, Function<DataValue, DataElement> readFunction) {
        return new Readable(identifier, el -> readFunction.apply(el.requireOf(DataValue.class)));
    }

    public static Readable map(String identifier, Function<DataMap, DataElement> readFunction) {
        return new Readable(identifier, el -> readFunction.apply(el.requireOf(DataMap.class)));
    }

    public static Readable map(String identifier, Function<DataMap, DataElement> readFunction, ReadableOptions options) {
        return new Readable(identifier, el -> readFunction.apply(options.read(el.requireOf(DataMap.class))));
    }

    public static Readable primitive(Class<?> valueClass) {
        return SINGLE_TYPE_CACHE.computeIfAbsent(valueClass, Readable::createPrimitive);
    }

    // PRIMITIVE

    private static Readable createPrimitive(Class<?> valueClass) {
        return new Readable(valueClass.getSimpleName().toLowerCase(), el ->
                el.requireOf(DataValue.class).requireType(valueClass)
        ).info(i -> i.description("Any " + valueClass.getSimpleName().toLowerCase()));
    }

    public static Readable string() {
        return primitive(String.class);
    }

    public static Readable integer() {
        return primitive(Integer.class);
    }

    public static Readable number() {
        return primitive(Number.class);
    }

    // ENUM

    public static <E extends Enum<E>> Readable enumeration(Class<E> enumClass) {
        return new Readable(enumClass.getSimpleName().toLowerCase(), el -> enumGetter(enumClass, el.requireOf(DataValue.class)))
                .info(i -> i.description("A " + enumClass.getSimpleName().toLowerCase()).possibleValues(Arrays.stream(enumClass.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
    }

    private static <E extends Enum<E>> DataValue enumGetter(Class<E> enumClass, DataValue value) {
        return value.requireType(String.class).andThen(enumClass, () -> {
            String input = value.valueUnsafe();
            try {
                return Enum.valueOf(enumClass, input);
            } catch (IllegalArgumentException e) {
                throw new ElementException(value, "'%s' does not contain value %s".formatted(enumClass.getSimpleName(), input));
            }
        });
    }

    // LIST (CACHED)

    private static Readable createList(DataType original) {
        return new Readable(original.friendlyId() + "_list", el -> {
            DataList list = el.requireOf(DataList.class);
            DataList results = Maple.list(list.size());
            list.forEach(listEl -> results.add(original.read(listEl)));
            return results;
        }).info(i -> i.description("A list of " + original.id()));
    }

    public static Readable list(DataType original) {
        return LIST_TYPE_CACHE.computeIfAbsent(original, Readable::createList);
    }

    @Override
    public DataElement read(DataElement element) {
        try {
            return readFunction.apply(element);
        } catch (ElementException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new ElementException(element, "error happened while reading dataType " + friendlyId(), throwable);
        }
    }

    // -- GET / SET

    @Override
    public String id() {
        return identifier;
    }

    @Override
    public Readable info(Consumer<DataTypeInfo> consumer) {
        DataType.super.info(consumer);
        return this;
    }

    @Override
    public DataTypeInfo info() {
        if (info == null) info = new DataTypeInfo(this);
        return info;
    }
}
