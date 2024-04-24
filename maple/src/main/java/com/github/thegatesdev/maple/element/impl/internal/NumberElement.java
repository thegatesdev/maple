package com.github.thegatesdev.maple.element.impl.internal;

import com.github.thegatesdev.maple.element.*;
import com.github.thegatesdev.maple.element.impl.*;

import java.math.*;

/**
 * Common implementations for number elements.
 */
public sealed interface NumberElement extends Element permits BigDecimalElement,
    BigIntegerElement,
    DoubleElement,
    FloatElement,
    IntElement,
    LongElement {

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
