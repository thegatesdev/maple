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

package io.github.thegatesdev.maple.read.types;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataValue;
import io.github.thegatesdev.maple.read.DataType;

import java.util.HashMap;
import java.util.Map;

/**
 * DataType representing an enum.
 * Caches all types.
 */
public class EnumDataType<E extends Enum<E>> implements DataType<DataValue<E>> {

    private static final Map<Class<Enum<?>>, EnumDataType<?>> CACHE = new HashMap<>();

    private final Class<E> valueType;
    private final String id;

    private EnumDataType(Class<E> valueType) {
        this.valueType = valueType;
        this.id = DataType.typeToIdentifier(valueType);
    }

    @SuppressWarnings("unchecked")
    public static <E extends Enum<E>> EnumDataType<E> getOrCreate(Class<E> valueType) {
        return (EnumDataType<E>) CACHE.computeIfAbsent((Class<Enum<?>>) valueType, EnumDataType::new);
    }

    private E enumFromString(String value) {
        return Enum.valueOf(valueType, value.toUpperCase().replaceAll("\\s+", "_"));
    }


    @Override
    public DataValue<E> read(DataElement input) {
        return input.asValue()
                .getAsHolding(String.class)
                .transform(valueType, this::enumFromString);
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public int hashCode() {
        return 524287 * valueType.hashCode();
    }
}
