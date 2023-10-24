package io.github.thegatesdev.maple.element;

import java.util.function.Function;

public final class StaticDataValue<Type> implements DataValue<Type> {

    private final Class<Type> valueType;
    private final Type value;

    @SuppressWarnings("unchecked")
    public StaticDataValue(Type value) {
        this.valueType = ((Class<Type>) value.getClass());
        this.value = value;
    }

    // Value

    @Override
    public <O> DataValue<O> transform(Class<O> newType, Function<Type, O> function) {
        return new StaticDataValue<>(function.apply(value));
    }

    @Override
    public Class<Type> getValueType() {
        return valueType;
    }

    @Override
    public Type getValue() {
        return value;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
