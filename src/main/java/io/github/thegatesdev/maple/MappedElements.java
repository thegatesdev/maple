package io.github.thegatesdev.maple;

import io.github.thegatesdev.maple.exception.ElementException;

import java.util.function.Consumer;

/*
Copyright (C) 2022  Timar Karels

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public interface MappedElements<Key> {

    DataElement getOrNull(Key key);

    DataElement get(Key key);

    // -- ELEMENT GETTERS


    // ANY

    /**
     * Runs the action if the specified element is present, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present.
     * @return This DataMap.
     */
    default MappedElements<Key> ifPresent(Key key, Consumer<DataElement> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null) action.accept(el);
        else if (elseAction != null) elseAction.run();
        return this;
    }

    /**
     * Runs the action if the specified element is present.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     * @return This DataMap.
     */
    default MappedElements<Key> ifPresent(Key key, Consumer<DataElement> action) {
        return ifPresent(key, action, null);
    }

    // VALUE

    /**
     * Get the value element associated with this key.
     *
     * @param key The key associated with the value element.
     * @return The found DataValue.
     * @throws ElementException If the element was not found, or the element was not a value element.
     */
    default DataValue getValue(Key key) throws ElementException {
        return get(key).requireOf(DataValue.class);
    }

    /**
     * Get the value element associated with this key, or a default.
     *
     * @param key The key associated with the value element.
     * @param def The value to return if the element is not a value element, or is not present.
     * @return The found DataValue, or the default value.
     */
    default DataValue getValue(Key key, DataValue def) {
        return get(key, DataValue.class, def);
    }

    /**
     * Runs the action if the specified element is present and is a DataValue.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     * @return This DataMap.
     */
    default MappedElements<Key> ifValue(Key key, Consumer<DataValue> action) {
        return ifValue(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataValue, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataValue.
     * @return This DataMap.
     */
    default MappedElements<Key> ifValue(Key key, Consumer<DataValue> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isValue()) action.accept(el.asValue());
        else if (elseAction != null) elseAction.run();
        return this;
    }

    /**
     * Get the value of the value element associated with this key, cast to P.
     *
     * @param key The key associated with the value element.
     * @param <P> The type to cast to.
     * @return The cast value.
     * @throws ElementException If the element was not found, or is not a value element.
     */
    default <P> P getUnsafe(Key key) throws ElementException {
        return getValue(key).valueUnsafe();
    }

    /**
     * Get the value of the value element associated with this key, cast to P, or a default value.
     * This does not return the default value if the cast throws!
     *
     * @param key The key associated with the primitive element.
     * @param <P> The type to cast to.
     * @param def The default value to return if the element is not a value element or is not present.
     * @return The cast value.
     */
    default <P> P getUnsafe(Key key, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isValue()) return def;
        final P val = el.asValue().valueUnsafe();
        return def == null ? val : (val == null ? def : val);
    }

    /**
     * Get the value of the value element associated with this key, or throw.
     *
     * @param key            The key of the value element.
     * @param primitiveClass The class the value should be of.
     * @param <P>            The type the value should be of.
     * @return The value of the value element.
     * @throws ElementException If the value element was not found, or the value did not conform to P.
     */
    default <P> P get(Key key, Class<P> primitiveClass) throws ElementException {
        return getValue(key).valueOrThrow(primitiveClass);
    }

    /**
     * Get the value of the value element associated with this key, or a default.
     *
     * @param key            The key of the value element.
     * @param primitiveClass The class the value should be of.
     * @param <P>            The type the value should be of.
     * @param def            The default value to return when the element is not present, not a value element, or the type does not match.
     * @return The value of the value element, or the default value.
     */
    default <P> P get(Key key, Class<P> primitiveClass, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isValue()) return def;
        final P val = el.asValue().valueOrNull(primitiveClass);
        return def == null ? val : (val == null ? def : val);
    }


    // MAP

    /**
     * Get the map element associated with this key.
     *
     * @param key The key associated with the map element.
     * @return The found DataMap.
     * @throws ElementException If the element was not found, or the element was not a map element.
     */
    default DataMap getMap(Key key) throws ElementException {
        return get(key).requireOf(DataMap.class);
    }

    /**
     * Get the map element associated with this key, or a default.
     *
     * @param key The key associated with the map element.
     * @param def The value to return if the element is not a map element, or is not present.
     * @return The found DataMap, or the default value.
     */
    default DataMap getMap(Key key, DataMap def) {
        return get(key, DataMap.class, def);
    }

    /**
     * Runs the action if the specified element is present and is a DataMap.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     * @return This DataMap.
     */
    default MappedElements<Key> ifMap(Key key, Consumer<DataMap> action) {
        return ifMap(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataMap, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataMap.
     * @return This DataMap.
     */
    default MappedElements<Key> ifMap(Key key, Consumer<DataMap> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isMap()) action.accept(el.asMap());
        else if (elseAction != null) elseAction.run();
        return this;
    }

    // LIST

    /**
     * Get the list element associated with this key.
     *
     * @param key The key associated with the list element.
     * @return The found DataList.
     * @throws ElementException If the element was not found, or the element was not a list element.
     */
    default DataList getList(Key key) throws ElementException {
        return get(key).requireOf(DataList.class);
    }

    /**
     * Get the list element associated with this key, or a default.
     *
     * @param key The key associated with the list element.
     * @param def The value to return if the element is not a list element, or is not present.
     * @return The found DataList, or the default value.
     */
    default DataList getList(Key key, DataList def) {
        return get(key, DataList.class, def);
    }

    /**
     * Runs the action if the specified element is present and is a DataList.
     *
     * @param key    The key to find the element at.
     * @param action The consumer to run when the element is found.
     * @return This DataMap.
     */
    default MappedElements<Key> ifList(Key key, Consumer<DataList> action) {
        return ifList(key, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataList, or the elseAction if not.
     *
     * @param key        The key to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataList.
     * @return This DataMap.
     */
    default MappedElements<Key> ifList(Key key, Consumer<DataList> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isMap()) action.accept(el.asList());
        else if (elseAction != null) elseAction.run();
        return this;
    }

    // VALUE GETTERS

    /**
     * Get the boolean value from the value element associated with this key.
     *
     * @param key The key associated with the element.
     * @return The boolean value of the value element.
     * @throws ElementException If the element was not a value element, or the value was not a boolean.
     */
    default Boolean getBoolean(Key key) throws ElementException {
        return get(key, Boolean.class);
    }

    /**
     * Get the boolean value from the value element associated with this key, or a default if the element is not present or the type does not match.
     *
     * @param key The key associated with the element.
     * @param def The default value.
     * @return The boolean value of the value element, or the default value.
     */
    default Boolean getBoolean(Key key, boolean def) {
        return get(key, Boolean.class, def);
    }

    /**
     * Get the integer value from the value element associated with this key.
     *
     * @param key The key associated with the element.
     * @return The integer value of the value element.
     * @throws ElementException If the element was not a value element, or the value was not an integer.
     */
    default Integer getInt(Key key) throws ElementException {
        return get(key, Integer.class);
    }

    /**
     * Get the integer value from the value element associated with this key, or a default if the element is not present or the type does not match.
     *
     * @param key The key associated with the element.
     * @param def The default value.
     * @return The integer value of the value element, or the default value.
     */
    default Integer getInt(Key key, int def) {
        return get(key, Integer.class, def);
    }

    /**
     * Get the double value from the value element associated with this key.
     *
     * @param key The key associated with the element.
     * @return The double value of the value element.
     * @throws ElementException If the element was not a value element, or the value was not a double.
     */
    default Double getDouble(Key key) throws ElementException {
        return get(key, Double.class);
    }

    /**
     * Get the double value from the value element associated with this key, or a default if the element is not present or the type does not match.
     *
     * @param key The key associated with the element.
     * @param def The default value.
     * @return The double value of the value element, or the default value.
     */
    default Double getDouble(Key key, double def) {
        return get(key, Double.class, def);
    }

    /**
     * Get the float value from the value element associated with this key.
     *
     * @param key The key associated with the element.
     * @return The float value of the value element.
     * @throws ElementException If the element was not a value element, or the value was not a float.
     */
    default Float getFloat(Key key) throws ElementException {
        return get(key, Float.class);
    }

    /**
     * Get the float value from the value element associated with this key, or a default if the element is not present or the type does not match.
     *
     * @param key The key associated with the element.
     * @param def The default value.
     * @return The float value of the value element, or the default value.
     */
    default Float getFloat(Key key, float def) {
        return get(key, Float.class, def);
    }

    /**
     * Get the long value from the value element associated with this key.
     *
     * @param key The key associated with the element.
     * @return The long value of the value element.
     * @throws ElementException If the element was not a value element, or the value was not a long.
     */
    default Long getLong(Key key) throws ElementException {
        return get(key, Long.class);
    }

    /**
     * Get the long value from the value element associated with this key, or a default if the element is not present or the type does not match.
     *
     * @param key The key associated with the element.
     * @param def The default value.
     * @return The long value of the value element, or the default value.
     */
    default Long getLong(Key key, long def) {
        return get(key, Long.class, def);
    }

    /**
     * Get the string value from the value element associated with this key.
     *
     * @param key The key associated with the element.
     * @return The string value of the value element.
     * @throws ElementException If the element was not a value element, or the value was not a string.
     */
    default String getString(Key key) throws ElementException {
        return get(key, String.class);
    }

    /**
     * Get the string value from the value element associated with this key, or a default if the element is not present or the type does not match.
     *
     * @param key The key associated with the element.
     * @param def The default value.
     * @return The string value of the value element, or the default value.
     */
    default String getString(Key key, String def) {
        return get(key, String.class, def);
    }
}
