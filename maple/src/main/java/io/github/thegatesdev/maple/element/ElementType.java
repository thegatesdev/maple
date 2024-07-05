package io.github.thegatesdev.maple.element;

/**
 * The types of elements that are available.
 * These all represent some value in the JSON specification.
 *
 * @author Timar Karels
 */
public enum ElementType {
    /**
     * A dictionary (also called mapping or object) of string keys to element values.
     * Equivalent of a JSON 'object'.
     */
    DICT,
    /**
     * A sequential list (also called array) of element values.
     * Equivalent of a JSON 'array'.
     */
    LIST,
    /**
     * A string value.
     * Equivalent of a JSON 'string'.
     */
    STRING,
    /**
     * A boolean value.
     * Equivalent of the JSON 'true' or 'false' values.
     */
    BOOL,
    /**
     * A number value.
     * Equivalent of the JSON 'number' value.
     * Number elements can store different number representations present in the Java language.
     * Currently present are: int, long, BigInteger, double, float and BigDecimal.
     */
    NUM,
    /**
     * An unset value.
     * Equivalent of the JSON 'null' value.
     * It is not advised to directly use this element type, it only exists to distinguish between absent entries and null values.
     */
    NULL
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
