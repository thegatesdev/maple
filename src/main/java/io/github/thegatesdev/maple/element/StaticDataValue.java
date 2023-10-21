package io.github.thegatesdev.maple.element;

public final class StaticDataValue<Type> implements DataValue<Type> {

    private final Class<Type> valueType;
    private final Type value;

    @SuppressWarnings("unchecked")
    public StaticDataValue(Type value) {
        this.valueType = ((Class<Type>) value.getClass());
        this.value = value;
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
