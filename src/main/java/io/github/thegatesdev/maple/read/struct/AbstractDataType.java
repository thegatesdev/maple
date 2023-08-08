package io.github.thegatesdev.maple.read.struct;

public abstract class AbstractDataType<E> implements DataType<E> {
    protected final String key;

    protected AbstractDataType(String key) {
        this.key = key;
    }

    @Override
    public String key() {
        return key;
    }
}
