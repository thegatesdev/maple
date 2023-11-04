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
import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.read.DataType;

import java.util.HashMap;
import java.util.Map;

/**
 * DataType representing a list of a given other datatype.
 * Caches all types.
 */
public class ListDataType implements DataType<DataList> {

    private static final Map<DataType<?>, ListDataType> CACHE = new HashMap<>();

    private final DataType<?> original;
    private final String id;

    private ListDataType(DataType<?> original) {
        this.original = original;
        this.id = original.getId() + "_list";
    }

    public static ListDataType getOrCreate(DataType<?> original) {
        return CACHE.computeIfAbsent(original, ListDataType::new);
    }


    @Override
    public DataList read(DataElement input) {
        return input.asList().transform(original::read);
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public int hashCode() {
        return 524287 * original.hashCode();
    }
}
