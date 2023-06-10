package io.github.thegatesdev.maple.registry;

import io.github.thegatesdev.maple.read.DataType;
import io.github.thegatesdev.maple.read.Factory;
import io.github.thegatesdev.maple.read.ReadableOptionsHolder;

import java.util.Collection;

public abstract class FactoryRegistry<Data, Fac extends Factory<? extends Data> & ReadableOptionsHolder> implements Identifiable, DataType<Data> {
    protected final String id;
    protected DataTypeInfo info;

    protected FactoryRegistry(String id) {
        this.id = id;
    }

    public abstract Collection<String> keys();

    public abstract Fac get(String key);

    @Override
    public String id() {
        return id;
    }

    @Override
    public DataTypeInfo info() {
        if (info == null) info = new DataTypeInfo(this);
        return info;
    }
}
