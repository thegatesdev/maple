package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class FloatElement implements Element {

    private static final FloatElement ZERO = new FloatElement(0f);

    private final float value;

    private FloatElement(float value) {
        this.value = value;
    }


    public static FloatElement of(float value) {
        if (value == 0f) return ZERO;
        return new FloatElement(value);
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
        if (!(other instanceof FloatElement doubleElement)) return false;
        return value == doubleElement.value;
    }
}
