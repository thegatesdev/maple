package io.github.thegatesdev.maple.registry;

import io.github.thegatesdev.maple.Maple;
import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.data.DataMap;
import io.github.thegatesdev.maple.data.DataValue;
import io.github.thegatesdev.maple.exception.ElementException;
import io.github.thegatesdev.maple.read.Factory;
import io.github.thegatesdev.maple.read.ReadableOptionsHolder;

import java.util.*;
import java.util.function.Function;

public class StaticFactoryRegistry<Data, Fac extends Factory<? extends Data> & ReadableOptionsHolder> extends FactoryRegistry<Data, Fac> {
    private final Function<Fac, String> keyGetter;

    private final Map<String, Fac> factories = new HashMap<>();
    private final Map<String, Fac> view = Collections.unmodifiableMap(factories);
    private int count = 0;

    protected StaticFactoryRegistry(String id, Function<Fac, String> keyGetter) {
        super(id);
        this.keyGetter = keyGetter;
    }

    // -- REGISTRATION

    public void registerStatic() {
    }

    public <T extends Fac> T register(final String key, final T value) {
        if (factories.putIfAbsent(key, value) != null)
            throw new RuntimeException("Factory with key '" + key + "' already exists");
        count++;
        return value;
    }

    public void register(Fac factory) {
        register(keyGetter.apply(factory), factory);
    }

    public void register(Collection<Fac> factories) {
        for (final Fac factory : factories) register(factory);
    }

    @SafeVarargs
    public final void register(Fac... factories) {
        for (final Fac factory : factories) {
            register(factory);
        }
    }

    // -- USE

    public Fac get(String key) {
        return factories.get(key);
    }

    @Override
    public DataValue read(final DataElement element) {
        final DataMap data = element.requireOf(DataMap.class);
        final String s = data.getString("type");
        final Factory<? extends Data> factory = factories.get(s);
        if (factory == null)
            throw new ElementException(data, "specified %s %s does not exist".formatted(id, s));
        return Maple.value(factory.build(data));
    }

    // -- GET/SET

    public final int count() {
        return count;
    }

    public Set<String> keys() {
        return view.keySet();
    }
}

