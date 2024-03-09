package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;
import com.github.thegatesdev.maple.element.impl.internal.NumberElement;

/**
 * An element representing a float value.
 * In most cases, it is recommended to use the {@link Element#of(float)} static construction method
 * instead of the constructor.
 *
 * @param value the contained float value
 * @author Timar Karels
 * @see ElementType#NUMBER
 */
public record FloatElement(float value) implements Element, NumberElement {

    /**
     * @see Element#of(float)
     */
    public static Element of(float value) {
        if (value == 0f) return IntElement.ZERO;
        return new FloatElement(value);
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
    public String toString() {
        return "number<" + value + "F>";
    }
}
