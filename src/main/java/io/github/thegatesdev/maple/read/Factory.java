package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.data.DataMap;

@FunctionalInterface
public interface Factory<G> {

    G build(DataMap data);
}
