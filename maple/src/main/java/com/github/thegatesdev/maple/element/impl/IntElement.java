package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;
import com.github.thegatesdev.maple.element.impl.internal.NumberElement;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * An element representing an integer value.
 * In most cases, it is recommended to use the {@link Element#of(int)} static construction method
 * instead of the constructor.
 *
 * @param value the contained integer value
 * @author Timar Karels
 * @see ElementType#NUMBER
 */
public record IntElement(int value) implements Element, NumberElement {

    /**
     * An integer element representing a 'zero' value.
     * Cached and re-used by the construction methods instead of creating new instances.
     */
    public static final IntElement ZERO = new IntElement(0);

    /**
     * @see Element#of(int)
     */
    public static Element of(int value) {
        if (value == 0) return ZERO;
        return new IntElement(value);
    }


    @Override
    public short getShort() {
        return (short) value;
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
    public BigInteger getBigInteger() {
        if (this == ZERO) return BigInteger.ZERO;
        return NumberElement.super.getBigInteger();
    }

    @Override
    public BigDecimal getBigDecimal() {
        if (this == ZERO) return BigDecimal.ZERO;
        return NumberElement.super.getBigDecimal();
    }

    @Override
    public String toString() {
        return "number<" + value + "I>";
    }
}
