package io.github.thegatesdev.maple.element;

import java.util.function.Function;

/**
 * An unchanging value element.
 *
 * @param <Type> the type of the contained value
 */
record StaticValue<Type>(Class<Type> valueType, Type value) implements DataValue<Type> {

    @Override
    public <O> DataValue<O> transform(Class<O> newType, Function<Type, O> function) {
        return new StaticValue<>(newType, function.apply(value));
    }
}
