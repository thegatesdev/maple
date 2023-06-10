package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.Maple;
import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.data.DataList;
import io.github.thegatesdev.maple.data.DataValue;
import io.github.thegatesdev.maple.exception.ElementException;
import io.github.thegatesdev.maple.registry.DataTypeInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class Readable<Value> implements DataType<Value> {
    private static final Map<DataType<?>, Readable<?>> LIST_TYPE_CACHE = new HashMap<>();

    private final Function<DataElement, DataElement> readFunction;
    private final String identifier;
    private DataTypeInfo info;


    public Readable(String identifier, final Function<DataElement, DataElement> readFunction) {
        this.identifier = identifier;
        this.readFunction = readFunction;
    }

    // -- INFO

    private static <T> Readable<T> single(Class<T> primitiveClass) {
        return new Readable<>(primitiveClass.getSimpleName().toLowerCase(), el ->
                el.requireOf(DataValue.class).requireType(primitiveClass)
        );
    }

    public static Readable<String> string() {
        return single(String.class);
    }

    // -- CREATION

    // SINGLE

    public static Readable<Number> number() {
        return single(Number.class);
    }

    public static <E extends Enum<E>> Readable<E> enumeration(Class<E> enumClass) {
        return new Readable<>(enumClass.getSimpleName().toLowerCase(), el -> enumGetter(enumClass, el.requireOf(DataValue.class)));
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

    private static Readable<?> createList(DataType<?> original) {
        return new Readable<>(original.friendlyId() + "_list", el -> {
            DataList list = el.requireOf(DataList.class);
            DataList results = Maple.list(list.size());
            list.forEach(listEl -> results.add(original.read(listEl)));
            return results;
        });
    }

    public static Readable<?> list(DataType<?> original) {
        return LIST_TYPE_CACHE.computeIfAbsent(original, Readable::createList);
    }

    // LIST ( CACHED )

    @Override
    public Readable<Value> info(Consumer<DataTypeInfo> consumer) {
        DataType.super.info(consumer);
        return this;
    }

    @Override
    public DataTypeInfo info() {
        if (info == null) info = new DataTypeInfo(this);
        return info;
    }

    //

    // --

    @Override
    public String id() {
        return identifier;
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
}
