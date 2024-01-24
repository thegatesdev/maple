package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

import java.util.Optional;

public final class LongElement implements Element {

    private final long value;

    public LongElement(long value) {
        this.value = value;
    }


    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public int getInt() {
        return (int) value;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public float getFloat() {
        return value;
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public Optional<Number> grabNumber() {
        return Optional.of(value);
    }

    @Override
    public ElementType type() {
        return ElementType.NUMBER;
    }
}
