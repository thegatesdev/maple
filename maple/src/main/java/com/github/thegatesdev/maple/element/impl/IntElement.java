package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class IntElement implements Element {

    private static final IntElement ZERO = new IntElement(0);

    private final int value;

    private IntElement(int value) {
        this.value = value;
    }


    public static IntElement of(int value) {
        if (value == 0) return ZERO;
        return new IntElement(value);
    }


    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public int getInt() {
        return value;
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
    public ElementType type() {
        return ElementType.NUMBER;
    }
}