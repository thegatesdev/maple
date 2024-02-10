package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class LongElement implements Element {

    private static final LongElement ZERO = new LongElement(0L);

    private final long value;

    private LongElement(long value) {
        this.value = value;
    }


    public static LongElement of(long value) {
        if (value == 0L) return ZERO;
        return new LongElement(value);
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

    @Override
    public String toString() {
        return "number<" + value + "L>";
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LongElement doubleElement)) return false;
        return value == doubleElement.value;
    }
}
