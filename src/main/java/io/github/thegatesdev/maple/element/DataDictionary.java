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

package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.exception.KeyNotPresentException;

import java.util.Objects;
import java.util.function.Consumer;

public sealed interface DataDictionary<Key> permits DataMap, DataList {

    // Simple operations

    DataElement getOrNull(Key key);

    default DataElement getOrThrow(Key key) throws KeyNotPresentException {
        var el = getOrNull(key);
        if (el == null) throw new KeyNotPresentException(Objects.toString(key));
        return el;
    }

    void set(Key key, DataElement element);

    // Typed getters

    default <T> T get(Key key, Class<T> valueType) {
        return getValue(key).getValueOrThrow(valueType);
    }

    default <T> T getOr(Key key, Class<T> valueType, T def) {
        var element = getOrNull(key);
        if (element == null || !element.isValue()) return def;
        return element.asValue().getValueOr(valueType, def);
    }


    default Boolean getBool(Key key) {
        return get(key, Boolean.class);
    }

    default Boolean getBool(Key key, Boolean def) {
        return getOr(key, Boolean.class, def);
    }

    default Integer getInt(Key key) {
        return get(key, Integer.class);
    }

    default Integer getInt(Key key, Integer def) {
        return getOr(key, Integer.class, def);
    }

    default Long getLong(Key key) {
        return get(key, Long.class);
    }

    default Long getLong(Key key, Long def) {
        return getOr(key, Long.class, def);
    }

    default Number getNum(Key key) {
        return get(key, Number.class);
    }

    default Number getNum(Key key, Number def) {
        return getOr(key, Number.class, def);
    }

    default String getString(Key key) {
        return get(key, String.class);
    }

    default String getString(Key key, String def) {
        return getOr(key, String.class, def);
    }

    // Element getters

    default DataMap getMap(Key key) {
        return getOrThrow(key).asMap();
    }

    default DataList getList(Key key) {
        return getOrThrow(key).asList();
    }

    default DataValue<?> getValue(Key key) {
        return getOrThrow(key).asValue();
    }

    // Iteration

    void each(Consumer<DataElement> elementConsumer);

    default void eachMap(Consumer<DataMap> mapConsumer) {
        each(element -> element.ifMap(mapConsumer));
    }

    default void eachList(Consumer<DataList> mapConsumer) {
        each(element -> element.ifList(mapConsumer));
    }

    default void eachValue(Consumer<DataValue<?>> mapConsumer) {
        each(element -> element.ifValue(mapConsumer));
    }

    // Information

    int size();
}
