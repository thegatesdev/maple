package io.github.thegatesdev.maple.maple.element.impl;

import io.github.thegatesdev.maple.maple.annotation.internal.*;
import io.github.thegatesdev.maple.maple.element.*;
import io.github.thegatesdev.maple.maple.io.*;

import java.util.*;

/**
 * An element representing a String value.
 *
 * @param value the contained String value
 * @author Timar Karels
 * @see ElementType#STRING
 */
@ValueClassCandidate
public record StringElement(String value) implements Element {

    /**
     * @see Element#of(String)
     */
    public static Element of(String value) {
        Objects.requireNonNull(value, "given value is null");

        return new StringElement(value);
    }


    @Override
    public void writeTo(Destination destination) {
        destination.value(value);
    }


    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public ElementType type() {
        return ElementType.STRING;
    }

    @Override
    public String toString() {
        return "string<" + value + ">";
    }

    @Override
    public String stringValue() {
        return value;
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
