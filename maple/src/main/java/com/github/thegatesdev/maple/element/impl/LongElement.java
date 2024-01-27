package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class LongElement implements Element {

    private final long value;

    private LongElement(long value) {
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
    public ElementType type() {
        return ElementType.NUMBER;
    }
}
