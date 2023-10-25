package io.github.thegatesdev.maple.read.types;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataValue;
import io.github.thegatesdev.maple.read.DataType;

import java.util.HashMap;
import java.util.Map;

public class ValueDataType<Val> implements DataType<DataValue<Val>> {

    private static final Map<Class<?>, ValueDataType<?>> CACHE = new HashMap<>();

    private final Class<Val> valueType;
    private final String id;

    private ValueDataType(Class<Val> valueType) {
        this.valueType = valueType;
        this.id = DataType.typeToIdentifier(valueType);
    }

    @SuppressWarnings("unchecked")
    public static <Val> ValueDataType<Val> getOrCreate(Class<Val> valueType) {
        return (ValueDataType<Val>) CACHE.computeIfAbsent(valueType, ValueDataType::new);
    }


    @Override
    public DataValue<Val> read(DataElement input) {
        return input.asValue().getAsHolding(valueType);
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public int hashCode() {
        return 31 * valueType.hashCode();
    }
}
