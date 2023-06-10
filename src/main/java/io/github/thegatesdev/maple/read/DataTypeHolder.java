package io.github.thegatesdev.maple.read;

@FunctionalInterface
public interface DataTypeHolder<Value> {
    DataType<Value> dataType();
}
