package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

/**
 * An element representing a double value.
 * In most cases, it is recommended to use the {@link Element#of(double)} static construction method
 * instead of the constructor.
 *
 * @param value the contained double value
 * @see ElementType#NUMBER
 */
public record DoubleElement(double value) implements Element {

    /**
     * @see Element#of(double)
     */
    public static Element of(double value) {
        if (value == 0d) return IntElement.ZERO;
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
}
