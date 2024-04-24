/*
Copyright 2024 Timar Karels

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.*;
import com.github.thegatesdev.maple.element.impl.internal.*;

import java.math.*;

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
