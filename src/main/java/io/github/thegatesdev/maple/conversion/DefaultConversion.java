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

package io.github.thegatesdev.maple.conversion;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.element.DataMap;
import io.github.thegatesdev.maple.element.DataValue;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This conversion is able to convert most existing Java types to their element equivalent.
 * Arrays, Lists, and optionally Iterables will be converted to {@link DataList}.
 * Maps will be converted to {@link DataMap}.
 * The remaining types will be wrapped in a {@link DataValue}.
 */
public final class DefaultConversion implements Conversion {

    private boolean useToStringKeys = false, convertIterable = true;

    /**
     * Indicate to use {@link Object#toString()} to convert and use potential non-String keys, instead of ignoring them.
     * Default is {@code false}.
     */
    public DefaultConversion useToStringKeys(boolean useToStringKeys) {
        this.useToStringKeys = useToStringKeys;
        return this;
    }

    /**
     * Indicate to convert {@link Iterable} entries to their {@link DataList} equivalent, instead of wrapping them in a {@link DataValue}.
     * Default is {@code true}.
     */
    public DefaultConversion convertIterable(boolean convertIterable) {
        this.convertIterable = convertIterable;
        return this;
    }


    private DataElement apply(Object object) {
        Objects.requireNonNull(object, "Cannot convert 'null'");
        if (object instanceof DataElement) throw new IllegalArgumentException("Cannot convert 'DataElement'");

        if (object instanceof Object[] someArray) return convertList(List.of(someArray));
        if (object instanceof Map<?, ?> someMap) return convertMap(someMap);
        if (object instanceof List<?> someList) return convertList(someList);
        if (convertIterable && object instanceof Iterable<?> someIterable) return convertList(List.of(someIterable));

        return DataValue.of(object);
    }

    public DataMap convertMap(Map<?, ?> someMap) {
        var output = DataMap.builder(someMap.size());
        someMap.forEach((key, value) -> {
            var result = apply(value);
            if (key instanceof String stringKey) output.add(stringKey, result);
            else if (useToStringKeys) output.add(key.toString(), result);
        });
        return output.build();
    }

    public DataList convertList(List<?> someList) {
        var output = DataList.builder(someList.size());
        for (Object value : someList) output.add(apply(value));
        return output.build();
    }
}
