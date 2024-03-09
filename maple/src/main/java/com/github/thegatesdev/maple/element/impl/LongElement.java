package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;
import com.github.thegatesdev.maple.element.impl.internal.NumberElement;

/**
 * An element representing a long value.
 * In most cases, it is recommended to use the {@link Element#of(long)} static construction method
 * instead of the constructor.
 *
 * @param value the contained long value
 * @author Timar Karels
 * @see ElementType#NUMBER
 */
public record LongElement(long value) implements Element, NumberElement {

    /**
     * @see Element#of(long)
     */
    public static Element of(long value) {
        if (value == 0L) return IntElement.ZERO;
        return new LongElement(value);
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
    public String toString() {
        return "number<" + value + "L>";
    }
}
