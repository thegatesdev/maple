package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class FloatElement implements Element {

    private final float value;

    private FloatElement(float value) {
        this.value = value;
    }


    public static Element of(float value) {
        if (value == 0f) return ShortElement.ZERO;
        return new FloatElement(value);
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
        return "number<" + value + "F>";
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof FloatElement floatElement)) return false;
        return value == floatElement.value;
    }
}
