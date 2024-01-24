package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

import java.util.Optional;

public final class DoubleElement implements Element {

    private final double value;

    public DoubleElement(double value) {
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
    public int getInt(int def) {
        return getInt();
    }

    @Override
    public long getLong() {
        return (long) value;
    }

    @Override
    public long getLong(long def) {
        return getLong();
    }

    @Override
    public float getFloat() {
        return (float) value;
    }

    @Override
    public float getFloat(float def) {
        return getFloat();
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public double getDouble(double def) {
        return getDouble();
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
