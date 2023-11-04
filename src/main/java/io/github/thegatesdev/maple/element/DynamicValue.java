package io.github.thegatesdev.maple.element;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An changing value element.
 *
 * @param <Type> the type of the supplied value
 */
record DynamicValue<Type>(Class<Type> valueType, Supplier<Type> valueSupplier) implements DataValue<Type> {

    @Override
    public <O> DataValue<O> transform(Class<O> newType, Function<Type, O> function) {
        return new DynamicValue<>(newType, () -> function.apply(valueSupplier.get()));
    }

    @Override
    public Type value() {
        return valueSupplier.get();
    }
}
