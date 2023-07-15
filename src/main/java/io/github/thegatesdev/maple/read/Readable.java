package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.data.DataList;
import io.github.thegatesdev.maple.data.DataMap;
import io.github.thegatesdev.maple.data.DataValue;
import io.github.thegatesdev.maple.exception.ElementException;
import io.github.thegatesdev.maple.read.struct.AbstractDataType;
import io.github.thegatesdev.maple.read.struct.DataType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

public class Readable<E extends DataElement> extends AbstractDataType<E> {
    private static final Map<Class<?>, Readable<?>> CACHE_PRIMITIVE_TYPES = new HashMap<>();
    private static final Map<DataType<?>, Readable<?>> CACHE_LIST_TYPES = new HashMap<>();

    private final Function<DataElement, E> readFunction;

    private Readable(String key, Function<DataElement, E> readFunction) {
        super(key);
        this.readFunction = readFunction;
    }

    private static String name(Class<?> anyClass) {
        return anyClass.getSimpleName().toLowerCase();
    }

    // -- CREATE

    public static <E extends DataElement> Readable<E> any(String identifier, Function<DataElement, E> readFunction) {
        return new Readable<>(identifier, readFunction);
    }

    public static <E extends DataElement> Readable<E> map(String identifier, Function<DataMap, E> readFunction) {
        return new Readable<>(identifier, element -> readFunction.apply(element.requireOf(DataMap.class)));
    }

    public static <E extends DataElement> Readable<E> map(String identifier, Function<DataMap, E> readFunction, ReadableOptions options) {
        return new Readable<>(identifier, element -> readFunction.apply(options.read(element.requireOf(DataMap.class))))
            .info(info -> info.readableOptions(options));
    }

    public static <E extends DataElement> Readable<E> list(String identifier, Function<DataList, E> readFunction) {
        return new Readable<>(identifier, element -> readFunction.apply(element.requireOf(DataList.class)));
    }

    public static <E extends DataElement> Readable<E> value(String identifier, Function<DataValue<?>, E> readFunction) {
        return new Readable<>(identifier, element -> readFunction.apply(element.requireOf(DataValue.class)));
    }


    private static <P> Readable<DataValue<P>> createPrimitive(Class<P> primitiveClass) {
        return value(name(primitiveClass), value -> value.requireType(primitiveClass));
    }

    @SuppressWarnings("unchecked")
    private static <P> Readable<DataValue<P>> primitive(Class<P> primitiveClass) {
        return ((Readable<DataValue<P>>) CACHE_PRIMITIVE_TYPES.computeIfAbsent(primitiveClass, Readable::createPrimitive));
    }


    public static Readable<DataValue<String>> string() {
        return primitive(String.class);
    }

    public static Readable<DataValue<Integer>> integer() {
        return primitive(Integer.class);
    }

    public static Readable<DataValue<Number>> number() {
        return primitive(Number.class);
    }

    public static Readable<DataValue<Boolean>> bool() {
        return primitive(Boolean.class);
    }


    private static Readable<DataList> createList(DataType<?> original) {
        return list(original.friendlyKey() + "_list", list -> {
            var out = new DataList(list.size());
            list.each(element -> out.add(original.read(element)));
            return out;
        });
    }

    @SuppressWarnings("unchecked")
    public static Readable<DataList> list(DataType<?> original) {
        return (Readable<DataList>) CACHE_LIST_TYPES.computeIfAbsent(original, Readable::createList);
    }


    public static <F extends Enum<F>> Readable<DataValue<F>> createEnum(Class<F> enumClass) {
        return value(name(enumClass), value -> value
            .requireType(String.class)
            .then(enumClass, s -> Enum.valueOf(enumClass, s.toUpperCase().replaceAll(" ", "_")))
        );
    }

    @SuppressWarnings("unchecked")
    public static <F extends Enum<F>> Readable<DataValue<F>> enumeration(Class<F> enumClass) {
        return (Readable<DataValue<F>>) CACHE_PRIMITIVE_TYPES.computeIfAbsent(enumClass, aClass -> createEnum(enumClass));
    }

    // -- ACTIONS

    @Override
    public E read(DataElement element) {
        try {
            return readFunction.apply(element);
        } catch (ElementException e) {
            throw e;
        } catch (Throwable throwable) {
            throw new ElementException(element, "error happened while reading dataType " + friendlyKey(), throwable);
        }
    }

    // -- GET / SET

    @Override
    public Readable<E> info(Consumer<Info> consumer) {
        consumer.accept(info());
        return this;
    }

    // -- META

    @Override
    public int hashCode() {
        return Objects.hash(readFunction, key, info);
    }
}
