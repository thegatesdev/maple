package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class DoubleElement implements Element {

    private final double value;

    private DoubleElement(double value) {
        this.value = value;
    }


    public static Element of(double value) {
        if (value == 0d) return ShortElement.ZERO;
        return new DoubleElement(value);
    }


    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public short getShort() {
        return (short) value;
    }

    @Override
    public int getInt() {
        return (int) value;
    }

    @Override
    public long getLong() {
        return (long) value;
    }

    @Override
    public float getFloat() {
        return (float) value;
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
        return "number<" + value + "D>";
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DoubleElement doubleElement)) return false;
        return value == doubleElement.value;
    }
}
