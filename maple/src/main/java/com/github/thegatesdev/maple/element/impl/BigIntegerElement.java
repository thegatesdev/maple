package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;
import com.github.thegatesdev.maple.element.impl.internal.NumberElement;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * An element representing a big integer value.
 * In most cases, it is recommended to use the {@link Element#of(BigInteger)} static construction method
 * instead of the constructor.
 *
 * @param value the contained big integer value
 * @author Timar Karels
 * @see ElementType#NUMBER
 */
public record BigIntegerElement(BigInteger value) implements NumberElement, Element {

    /**
     * @see Element#of(BigInteger)
     */
    public static Element of(BigInteger value) {
        if (value.signum() == 0) return IntElement.ZERO;
        return new BigIntegerElement(value);
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
        return value;
    }

    @Override
    public BigDecimal getBigDecimal() {
        return new BigDecimal(value);
    }

    @Override
    public String toString() {
        return "number<" + value + "BI>";
    }
}
