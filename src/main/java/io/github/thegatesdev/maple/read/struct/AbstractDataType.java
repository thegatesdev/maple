package io.github.thegatesdev.maple.read.struct;

import io.github.thegatesdev.maple.data.DataElement;

public abstract class AbstractDataType<E extends DataElement> implements DataType<E> {
    protected final Info info;
    protected final String key;

    protected AbstractDataType(String key) {
        this.key = key;
        this.info = new Info(key);
    }

    @Override
    public String key() {
        return key;
    }

    @Override
    public Info info() {
        return info;
    }
}
