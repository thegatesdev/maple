/*
Copyright 2023 Timar Karels

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

package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.read.types.EnumDataType;
import io.github.thegatesdev.maple.read.types.ListDataType;
import io.github.thegatesdev.maple.read.types.ValueDataType;

/**
 * Defines a type of data, represented as an element, that can be read from another element.
 */
public interface DataType<R extends DataElement> {

    /**
     * Read the given input element to an element representing the type {@code R}.
     *
     * @param input the element to read from
     * @return an element of type {@code R}
     */
    R read(DataElement input);

    /**
     * Get the identifier of this datatype.
     *
     * @return the identifier of the datatype.
     */
    String getId();

    /**
     * Get a datatype representing a list of elements of this type.
     *
     * @return the list datatype
     */
    default ListDataType listType() {
        return DataType.list(this);
    }


    /**
     * Generate an identifier unique to the given class, in snake_case.
     *
     * @param type the type to get the identifier from
     * @return the identifier
     */
    static String typeToIdentifier(Class<?> type) {
        return type.getSimpleName().replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    // Creation

    /**
     * Get a datatype representing a single value of the given value type.
     *
     * @param valueType the type of the value
     * @return a cached datatype representing the given type
     */
    static <Val> ValueDataType<Val> value(Class<Val> valueType) {
        return ValueDataType.getOrCreate(valueType);
    }

    /**
     * Get the datatype representing a {@code String}.
     *
     * @return the cached datatype
     */
    static ValueDataType<String> string() {
        return value(String.class);
    }

    /**
     * Get the datatype representing a {@code Integer}.
     *
     * @return the cached datatype
     */
    static ValueDataType<Integer> integer() {
        return value(Integer.class);
    }

    /**
     * Get the datatype representing a {@code Number}.
     *
     * @return the cached datatype
     */
    static ValueDataType<Number> number() {
        return value(Number.class);
    }

    /**
     * Get the datatype representing a {@code Boolean}.
     *
     * @return the cached datatype
     */
    static ValueDataType<Boolean> bool() {
        return value(Boolean.class);
    }

    /**
     * Get a datatype representing the given enumeration type.
     * This reads a {@code String} value.
     *
     * @param valueType the type of the value
     * @return a cached datatype representing values of the enum
     */
    static <E extends Enum<E>> EnumDataType<E> enumeration(Class<E> valueType) {
        return EnumDataType.getOrCreate(valueType);
    }

    /**
     * Get a datatype representing a list of elements of the given original type.
     *
     * @return the list datatype
     */
    static ListDataType list(DataType<?> original) {
        return ListDataType.getOrCreate(original);
    }
}
