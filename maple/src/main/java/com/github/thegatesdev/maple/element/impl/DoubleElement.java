package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;
import com.github.thegatesdev.maple.element.impl.internal.NumberElement;

/**
 * An element representing a double value.
 * In most cases, it is recommended to use the {@link Element#of(double)} static construction method
 * instead of the constructor.
 *
 * @param value the contained double value
 * @author Timar Karels
 * @see ElementType#NUMBER
 */
public record DoubleElement(double value) implements NumberElement, Element {

    /**
     * @see Element#of(double)
     */
    public static Element of(double value) {
        if (value == 0d) return IntElement.ZERO;
        return new DoubleElement(value);
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
    public String toString() {
        return "number<" + value + "D>";
    }
}
