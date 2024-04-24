package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.*;
import com.github.thegatesdev.maple.element.impl.internal.*;

import java.math.*;

/**
 * An element representing a big decimal value.
 * In most cases, it is recommended to use the {@link Element#of(BigDecimal)} static construction method
 * instead of the constructor.
 *
 * @param value the contained big decimal value
 * @author Timar Karels
 * @see ElementType#NUMBER
 */
public record BigDecimalElement(BigDecimal value) implements NumberElement, Element {

    /**
     * @see Element#of(BigDecimal)
     */
    public static Element of(BigDecimal value) {
        if (value.signum() == 0) return IntElement.ZERO;
        return new BigDecimalElement(value);
    }


    @Override
    public short getShort() {
        return value.shortValue();
    }

    @Override
    public int getInt() {
        return value.intValue();
    }

    @Override
    public long getLong() {
        return value.longValue();
    }

    @Override
    public float getFloat() {
        return value.floatValue();
    }

    @Override
    public double getDouble() {
        return value.doubleValue();
    }

    @Override
    public BigInteger getBigInteger() {
        return value.toBigInteger();
    }

    @Override
    public BigDecimal getBigDecimal() {
        return value;
    }

    @Override
    public String toString() {
        return "number<" + value + "BD>";
    }
}
