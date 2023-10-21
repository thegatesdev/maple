package io.github.thegatesdev.maple.element;

import java.util.function.Supplier;

public final class DynamicDataValue<Type> implements DataValue<Type> {

    private final Class<Type> valueType;
    private final Supplier<Type> valueSupplier;

    public DynamicDataValue(Class<Type> valueType, Supplier<Type> valueSupplier) {
        this.valueType = valueType;
        this.valueSupplier = valueSupplier;
    }

    // Value

    @Override
    public Class<Type> getValueType() {
        return valueType;
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
