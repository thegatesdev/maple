package io.github.thegatesdev.maple.read.types;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataValue;
import io.github.thegatesdev.maple.read.DataType;

import java.util.HashMap;
import java.util.Map;

public class EnumDataType<E extends Enum<E>> implements DataType<DataValue<E>> {

    private static final Map<Class<Enum<?>>, EnumDataType<?>> CACHE = new HashMap<>();

    private final Class<E> valueType;
    private final String id;

    private EnumDataType(Class<E> valueType) {
        this.valueType = valueType;
        this.id = DataType.typeToIdentifier(valueType);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> EnumDataType<E> getOrCreate(Class<E> valueType) {
        return (EnumDataType<E>) CACHE.computeIfAbsent((Class<Enum<?>>) valueType, EnumDataType::new);
    }

    private E enumFromString(String value) {
        return Enum.valueOf(valueType, value.toUpperCase().replaceAll("\\s+", "_"));
    }


    @Override
    public DataValue<E> read(DataElement input) {
        return input.asValue()
                .getAsHolding(String.class)
                .transform(valueType, this::enumFromString);
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public int hashCode() {
        return 524287 * valueType.hashCode();
    }
}
