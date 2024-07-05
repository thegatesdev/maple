package io.github.thegatesdev.maple.maple.element.impl;

import io.github.thegatesdev.maple.maple.element.*;
import io.github.thegatesdev.maple.maple.io.*;

/**
 * An element representing an unset or 'null' value.
 *
 * @author Timar Karels
 * @see ElementType#NULL
 */
public enum NullElement implements Element {
    INSTANCE; // I started to really love enums for its unintended use-cases while making this.


    @Override
    public void writeTo(Destination destination) {
        destination.valueNull();
    }


    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public ElementType type() {
        return ElementType.NULL;
    }

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public String stringValue() {
        return "null";
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
