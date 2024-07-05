package io.github.thegatesdev.maple.element.impl;

import io.github.thegatesdev.maple.annotation.internal.*;
import io.github.thegatesdev.maple.element.*;
import io.github.thegatesdev.maple.element.impl.internal.*;
import io.github.thegatesdev.maple.io.*;

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
    public void writeTo(Destination destination) {
        destination.value(value);
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
