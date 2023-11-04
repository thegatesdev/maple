package io.github.thegatesdev.maple.element;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An changing value element.
 *
 * @param <Type> the type of the supplied value
 */
final class DynamicValue<Type> implements DataValue<Type> {

    private final Supplier<Type> valueSupplier;
    private final Class<Type> valueType;

    DynamicValue(Supplier<Type> supplier, Class<Type> type) {
        valueSupplier = supplier;
        valueType = type;
    }


    @Override
    public Class<Type> getValueType() {
        return valueType;
    }

    @Override
    public <O> DataValue<O> transform(Class<O> newType, Function<Type, O> function) {
        return new DynamicValue<>(() -> function.apply(valueSupplier.get()), newType);
    }

    @Override
    public Type getValue() {
        return valueSupplier.get();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
