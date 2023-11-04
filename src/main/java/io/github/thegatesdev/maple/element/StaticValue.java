package io.github.thegatesdev.maple.element;

import java.util.function.Function;

/**
 * An unchanging value element.
 *
 * @param <Type> the type of the contained value
 */
final class StaticValue<Type> implements DataValue<Type>{

    private final Type value;
    private final Class<Type> valueType;

    StaticValue(Type value, Class<Type> type) {
        this.value = value;
        valueType = type;
    }


    @Override
    public Class<Type> getValueType() {
        return valueType;
    }

    @Override
    public <O> DataValue<O> transform(Class<O> newType, Function<Type, O> function) {
        return new StaticValue<>(function.apply(value), newType);
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
