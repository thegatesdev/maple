package io.github.thegatesdev.maple.element.impl;

import io.github.thegatesdev.maple.annotation.internal.*;
import io.github.thegatesdev.maple.element.*;
import io.github.thegatesdev.maple.element.impl.internal.*;
import io.github.thegatesdev.maple.io.*;

/**
 * An element representing a float value.
 * In most cases, it is recommended to use the {@link Element#of(float)} static construction method
 * instead of the constructor.
 *
 * @param value the contained float value
 * @author Timar Karels
 * @see ElementType#NUM
 */
@ValueClassCandidate
public record FloatElement(float value) implements Element, NumberElement {

    /**
     * @see Element#of(float)
     */
    public static Element of(float value) {
        if (value == 0f) return IntElement.ZERO;
        return new FloatElement(value);
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

    @Override
    public String stringValue() {
        return Float.toString(value);
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
