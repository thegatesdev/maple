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

    /**
     * Get the element at the given key, or {@code null} if it isn't found.
     *
     * @param key the key for the element
     * @return the element mapped to the key
     */
    DataElement getOrNull(Key key);

    /**
     * Get the element at the given key.
     *
     * @param key the key for the element
     * @return the element mapped to the key
     * @throws KeyNotPresentException if the key was not found
     */
    default DataElement getOrThrow(Key key) throws KeyNotPresentException {
        var el = getOrNull(key);
        if (el == null) throw new KeyNotPresentException(Objects.toString(key));
        return el;
    }

    // Typed getters

    /**
     * Get a value of type {@code T} from the value element at the given key.
     *
     * @param key       the key for the element
     * @param valueType the type the value should be of
     * @return the value of type {@code T}
     * @throws KeyNotPresentException                                     if the key was not found
     * @throws io.github.thegatesdev.maple.exception.ElementTypeException if the element mapped to the key was not a value element
     * @throws io.github.thegatesdev.maple.exception.ValueTypeException   if the value element did not contain a value of the given type
     */
    default <T> T get(Key key, Class<T> valueType) {
        return getValue(key).getValueOrThrow(valueType);
    }

    /**
     * Get a value of type {@code T} from the value element at the given key.
     * Returns the given default value when the key is not found,
     * the element is not a value element,
     * or the value element did not contain a value of the given type.
     *
     * @param key       the key for the element
     * @param valueType the type the value should be of
     * @param def       the default value to return
     * @return the value of type {@code T}
     */
    default <T> T getOr(Key key, Class<T> valueType, T def) {
        var element = getOrNull(key);
        if (element == null || !element.isValue()) return def;
        return element.asValue().getValueOr(valueType, def);
    }


    /**
     * Get the {@code Boolean} value from the value element at the given key.
     *
     * @param key the key for the element
     * @return the value from the element
     */
    default Boolean getBool(Key key) {
        return get(key, Boolean.class);
    }

    /**
     * Get the {@code Boolean} value from the value element at the given key.
     * Returns the given default value when it is not present
     *
     * @param key the key for the element
     * @param def the default value
     * @return the value from the element
     */
    default Boolean getBool(Key key, Boolean def) {
        return getOr(key, Boolean.class, def);
    }

    /**
     * Get the {@code Integer} value from the value element at the given key.
     *
     * @param key the key for the element
     * @return the value from the element
     */
    default Integer getInt(Key key) {
        return get(key, Integer.class);
    }

    /**
     * Get the {@code Integer} value from the value element at the given key.
     * Returns the given default value when it is not present
     *
     * @param key the key for the element
     * @param def the default value
     * @return the value from the element
     */
    default Integer getInt(Key key, Integer def) {
        return getOr(key, Integer.class, def);
    }

    /**
     * Get the {@code Long} value from the value element at the given key.
     *
     * @param key the key for the element
     * @return the value from the element
     */
    default Long getLong(Key key) {
        return get(key, Long.class);
    }

    /**
     * Get the {@code Long} value from the value element at the given key.
     * Returns the given default value when it is not present
     *
     * @param key the key for the element
     * @param def the default value
     * @return the value from the element
     */
    default Long getLong(Key key, Long def) {
        return getOr(key, Long.class, def);
    }

    /**
     * Get the {@code Number} value from the value element at the given key.
     *
     * @param key the key for the element
     * @return the value from the element
     */
    default Number getNum(Key key) {
        return get(key, Number.class);
    }

    /**
     * Get the {@code Number} value from the value element at the given key.
     * Returns the given default value when it is not present
     *
     * @param key the key for the element
     * @param def the default value
     * @return the value from the element
     */
    default Number getNum(Key key, Number def) {
        return getOr(key, Number.class, def);
    }

    /**
     * Get the {@code String} value from the value element at the given key.
     *
     * @param key the key for the element
     * @return the value from the element
     */
    default String getString(Key key) {
        return get(key, String.class);
    }

    /**
     * Get the {@code String} value from the value element at the given key.
     * Returns the given default value when it is not present
     *
     * @param key the key for the element
     * @param def the default value
     * @return the value from the element
     */
    default String getString(Key key, String def) {
        return getOr(key, String.class, def);
    }

    // Element getters

    /**
     * Get the map element at the given key.
     *
     * @param key the key for the element
     * @return the map element
     */
    default DataMap getMap(Key key) {
        return getOrThrow(key).asMap();
    }

    /**
     * Get the list element at the given key.
     *
     * @param key the key for the element
     * @return the list element
     */
    default DataList getList(Key key) {
        return getOrThrow(key).asList();
    }

    /**
     * Get the value element at the given key.
     *
     * @param key the key for the element
     * @return the value element
     */
    default DataValue<?> getValue(Key key) {
        return getOrThrow(key).asValue();
    }

    // Iteration


    /**
     * Run the given action for each element in this dictionary.
     *
     * @param elementConsumer the action to run
     */
    void each(Consumer<DataElement> elementConsumer);

    /**
     * Run the given action for each map element in this dictionary.
     *
     * @param mapConsumer the action to run
     */
    default void eachMap(Consumer<DataMap> mapConsumer) {
        each(element -> element.ifMap(mapConsumer));
    }

    /**
     * Run the given action for each list element in this dictionary.
     *
     * @param listConsumer the action to run
     */
    default void eachList(Consumer<DataList> listConsumer) {
        each(element -> element.ifList(listConsumer));
    }

    /**
     * Run the given action for each value element in this dictionary.
     *
     * @param valueConsumer the action to run
     */
    default void eachValue(Consumer<DataValue<?>> valueConsumer) {
        each(element -> element.ifValue(valueConsumer));
    }

    // Information

    /**
     * @return the amount of elements in this dictionary
     */
    int size();
}
