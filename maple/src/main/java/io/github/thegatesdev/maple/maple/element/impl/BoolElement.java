package io.github.thegatesdev.maple.maple.element.impl;

import io.github.thegatesdev.maple.maple.element.*;
import io.github.thegatesdev.maple.maple.io.*;

/**
 * An element representing a boolean value.
 *
 * @author Timar Karels
 * @see ElementType#BOOL
 */
public enum BoolElement implements Element {
    TRUE, FALSE;

    /**
     * @see Element#of(boolean)
     */
    public static Element of(boolean value) {
        return value ? TRUE : FALSE;
    }


    @Override
    public void writeTo(Destination destination) {
        destination.value(getBool());
    }


    @Override
    public boolean isBool() {
        return true;
    }

    @Override
    public boolean getBool() {
        // I wonder what difference this makes to storing a boolean variable and returning that...
        // This would technically only require an address check vs actually looking up the stored boolean data.
        // Just a funny thought, the difference won't ever be noticeable.
        return this == TRUE;
    }

    @Override
    public ElementType type() {
        return ElementType.BOOL;
    }

    @Override
    public String toString() {
        return "bool<" + stringValue() + ">";
    }

    @Override
    public String stringValue() {
        return Boolean.toString(getBool());
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
