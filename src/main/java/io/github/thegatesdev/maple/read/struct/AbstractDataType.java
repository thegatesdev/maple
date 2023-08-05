package io.github.thegatesdev.maple.read.struct;

public abstract class AbstractDataType<E> implements DataType<E> {
    private final Info info;
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
