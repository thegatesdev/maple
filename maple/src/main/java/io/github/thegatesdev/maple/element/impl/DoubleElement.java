package io.github.thegatesdev.maple.element.impl;

import io.github.thegatesdev.maple.annotation.internal.*;
import io.github.thegatesdev.maple.element.*;
import io.github.thegatesdev.maple.element.impl.internal.*;
import io.github.thegatesdev.maple.io.*;

/**
 * An element representing a double value.
 * In most cases, it is recommended to use the {@link Element#of(double)} static construction method
 * instead of the constructor.
 *
 * @param value the contained double value
 * @author Timar Karels
 * @see ElementType#NUM
 */
@ValueClassCandidate
public record DoubleElement(double value) implements NumberElement, Element {

    /**
     * @see Element#of(double)
     */
    public static Element of(double value) {
        if (value == 0d) return IntElement.ZERO;
        return new DoubleElement(value);
    }


    @Override
    public void writeTo(Destination destination) {
        destination.value(value);
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

    @Override
    public String stringValue() {
        return Double.toString(value);
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
