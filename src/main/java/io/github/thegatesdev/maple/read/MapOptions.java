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

/**
 * Applies options to a {@code DataMap}, throwing on incorrect input.
 */
public final class MapOptions<Ret> {

    private final Option<?>[] entries;
    private final Function<DataMap, Ret> conversion;

    private MapOptions(Option<?>[] entries, Function<DataMap, Ret> conversion) {
        this.entries = entries;
        this.conversion = conversion;
    }

    /**
     * Creates a new map option builder with the given result conversion function.
     *
     * @param conversion the result conversion function
     * @return a new builder
     */
    public static <Ret> Builder<Ret> builder(Function<DataMap, Ret> conversion) {
        return new Builder<>(conversion);
    }

    /**
     * Creates a new map option builder, without a result conversion.
     *
     * @return a new builder
     */
    public static Builder<DataMap> builder() {
        return builder(Function.identity());
    }


    /**
     * Apply the options to the given input map.
     * This creates a new map storing the results of the applied options.
     *
     * @param input the input map
     * @return the value after applying the result conversion to the generated map
     */
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

    /**
     * Get an immutable view of the option entries.
     *
     * @return the option entries
     */
    public List<Option<?>> getEntries() {
        return List.of(entries);
    }


    /**
     * An option for a map.
     *
     * @param key          the unique key in the map
     * @param dataType     the expected datatype of the value that will be read
     * @param hasDefault   if the option has a default value
     * @param defaultValue the default value of the option
     */
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

    /**
     * A map options builder.
     */
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

        /**
         * Build map options from the current state of the builder.
         *
         * @return a new {@code MapOptions} containing the current specified options
         */
        public MapOptions<Ret> build() {
            return new MapOptions<>(buildingOptions.toArray(new Option[0]), conversion);
        }


        /**
         * Add a new required option.
         *
         * @param key      the unique key for the option
         * @param dataType the datatype of the value
         */
        public Builder<Ret> add(String key, DataType<?> dataType) {
            return add(new Option<>(key, dataType));
        }

        /**
         * Add a new optional option.
         *
         * @param key      the unique key for the option
         * @param dataType the datatype of the value
         */
        public Builder<Ret> optional(String key, DataType<?> dataType) {
            return add(new Option<>(key, dataType, null));
        }

        /**
         * Add a new optional option using the given default value.
         *
         * @param key      the unique key for the option
         * @param dataType the datatype of the value
         * @param def      the default element to insert if the key is not present
         */
        public <Type extends DataElement> Builder<Ret> add(String key, DataType<Type> dataType, Type def) {
            return add(new Option<>(key, dataType, Objects.requireNonNull(def)));
        }

        /**
         * Add a new optional option for a value element type using the given default value.
         * This is a convenience method to not have to supply a default element like {@code new StaticDataValue<>(yourDefault)}.
         *
         * @param key      the unique key for the option
         * @param dataType the value element datatype of the value
         * @param def      the default value to insert if the key is not present
         */
        public <Val> Builder<Ret> add(String key, DataType<DataValue<Val>> dataType, Val def) {
            return add(key, dataType, new StaticDataValue<>(def));
        }
    }
}
