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
import io.github.thegatesdev.maple.element.DataMap;
import io.github.thegatesdev.maple.element.DataValue;
import io.github.thegatesdev.maple.element.StaticDataValue;
import io.github.thegatesdev.maple.exception.KeyNotPresentException;

import java.util.*;
import java.util.function.Function;

public final class MapOptions<Ret> {

    private final Option<?>[] entries;
    private final Function<DataMap, Ret> conversion;

    private MapOptions(Option<?>[] entries, Function<DataMap, Ret> conversion) {
        this.entries = entries;
        this.conversion = conversion;
    }

    public static <Ret> Builder<Ret> builder(Function<DataMap, Ret> conversion) {
        return new Builder<>(conversion);
    }

    public static Builder<DataMap> builder() {
        return builder(Function.identity());
    }


    public Ret apply(DataMap input) {
        var output = new DataMap(entries.length);
        for (final var entry : entries) {
            var element = input.getOrNull(entry.key);
            if (element != null) output.set(entry.key, entry.dataType.read(element));
            else {
                if (entry.hasDefault) throw new KeyNotPresentException(entry.key);
                if (entry.defaultValue == null) continue;
                output.set(entry.key, entry.defaultValue);
            }
        }
        return conversion.apply(output);
    }

    public List<Option<?>> getEntries() {
        return List.of(entries);
    }


    public record Option<Type extends DataElement>(String key, DataType<Type> dataType, boolean hasDefault,
                                                   Type defaultValue) {

        public Option {
            Objects.requireNonNull(key);
            Objects.requireNonNull(dataType);
            Objects.requireNonNull(defaultValue);
        }

        public Option(String key, DataType<Type> dataType) {
            this(key, dataType, false, null);
        }

        public Option(String key, DataType<Type> dataType, Type defaultValue) {
            this(key, dataType, true, defaultValue);
        }
    }

    public static final class Builder<Ret> {

        private final List<Option<?>> buildingOptions = new ArrayList<>();
        private final Set<String> uniqueKeys = new HashSet<>();
        private final Function<DataMap, Ret> conversion;

        private Builder(Function<DataMap, Ret> conversion) {
            this.conversion = conversion;
        }


        private Builder<Ret> add(Option<?> option) {
            if (!uniqueKeys.add(option.key()))
                throw new IllegalArgumentException("Trying to add duplicate option key: " + option.key());
            buildingOptions.add(option);
            return this;
        }

        public MapOptions<Ret> build() {
            return new MapOptions<>(buildingOptions.toArray(new Option[0]), conversion);
        }


        public Builder<Ret> add(String key, DataType<?> dataType) {
            return add(new Option<>(key, dataType));
        }

        public Builder<Ret> optional(String key, DataType<?> holder) {
            return add(new Option<>(key, holder, null));
        }

        public <Type extends DataElement> Builder<Ret> add(String key, DataType<Type> holder, Type def) {
            return add(new Option<>(key, holder, Objects.requireNonNull(def)));
        }

        public <Val> Builder<Ret> add(String key, DataType<DataValue<Val>> holder, Val def) {
            return add(key, holder, new StaticDataValue<>(def));
        }
    }
}
