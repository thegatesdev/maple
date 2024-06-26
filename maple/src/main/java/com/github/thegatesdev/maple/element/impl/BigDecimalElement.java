package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.annotation.internal.*;
import com.github.thegatesdev.maple.element.*;
import com.github.thegatesdev.maple.element.impl.internal.*;

import java.math.*;
import java.util.*;

/**
 * An element representing a big decimal value.
 * In most cases, it is recommended to use the {@link Element#of(BigDecimal)} static construction method
 * instead of the constructor.
 *
 * @param value the contained big decimal value
 * @author Timar Karels
 * @see ElementType#NUM
 */
@ValueClassCandidate
public record BigDecimalElement(BigDecimal value) implements NumberElement, Element {

    /**
     * @see Element#of(BigDecimal)
     */
    public static Element of(BigDecimal value) {
        Objects.requireNonNull(value, "given value is null");

        if (value.signum() == 0) return IntElement.ZERO;
        return new BigDecimalElement(value);
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

    @Override
    public String stringValue() {
        return value.toString();
    }
}
