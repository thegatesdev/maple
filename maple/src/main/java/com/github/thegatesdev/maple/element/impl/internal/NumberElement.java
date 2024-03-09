package com.github.thegatesdev.maple.element.impl.internal;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;
import com.github.thegatesdev.maple.element.impl.DoubleElement;
import com.github.thegatesdev.maple.element.impl.FloatElement;
import com.github.thegatesdev.maple.element.impl.IntElement;
import com.github.thegatesdev.maple.element.impl.LongElement;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Common implementations for number elements.
 */
public sealed interface NumberElement extends Element permits DoubleElement, FloatElement, IntElement, LongElement {

    @Override
    default boolean isNumber() {
        return true;
    }

    @Override
    default BigInteger getBigInteger() {
        return BigInteger.valueOf(getLong());
    }

    @Override
    default BigDecimal getBigDecimal() {
        return BigDecimal.valueOf(getDouble());
    }

    @Override
    default ElementType type() {
        return ElementType.NUMBER;
    }
}