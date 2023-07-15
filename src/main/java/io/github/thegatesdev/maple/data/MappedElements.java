package io.github.thegatesdev.maple.data;

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

/**
 * An element mapping values to {@code <Key>}.
 */
public interface MappedElements<Key> {

    /**
     * Get the element at the supplied key, or null if it isn't present.
     */
    DataElement getOrNull(Key key);

    /**
     * Get the element at the supplied key, or a new DataNull if it isn't present.
     */
    DataElement get(Key key);

    // -- ELEMENT GETTERS


    // ANY

    /**
     * Run the supplied action on the element at the supplied key, or the elseAction if it isn't present.
     */
    default void ifPresent(Key key, Consumer<DataElement> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null) action.accept(el);
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Run the supplied action on the element at the supplied key if it is present.
     */
    default void ifPresent(Key key, Consumer<DataElement> action) {
        ifPresent(key, action, null);
    }

    // VALUE

    /**
     * Get the value element at the supplied key.
     *
     * @throws ElementException If the value element was not found
     */
    default DataValue<?> getValue(Key key) throws ElementException {
        return get(key).requireOf(DataValue.class);
    }

    /**
     * Get the value element at the supplied key.
     * Returns the supplied default value if the value element was not found.
     */
    default DataValue<?> getValue(Key key, DataValue<?> def) {
        return getElement(key, DataValue.class, def);
    }

    default <T> DataValue<T> getValueOf(Key key, Class<T> valueType) {
        return getValue(key).requireType(valueType);
    }

    /**
     * Run the supplied action on the value element at the supplied key, or the elseAction if no value element is found.
     */
    default MappedElements<Key> ifValue(Key key, Consumer<DataValue<?>> action) {
        return ifValue(key, action, null);
    }

    /**
     * Run the supplied action on the value element at the supplied key.
     */
    default MappedElements<Key> ifValue(Key key, Consumer<DataValue<?>> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isValue()) action.accept(el.asValue());
        else if (elseAction != null) elseAction.run();
        return this;
    }

    /**
     * Get the value of the value element at the supplied key, cast to {@code P}.
     *
     * @throws ElementException If the value element was not found
     */
    default <P> P getUnsafe(Key key) throws ElementException {
        return getValue(key).valueUnsafe();
    }

    /**
     * Get the value of the value element at the supplied key, cast to {@code P}.
     * Returns the supplied default value if the value element was not found.
     */
    default <P> P getUnsafe(Key key, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isValue()) return def;
        final P val = el.asValue().valueUnsafe();
        return def == null ? val : (val == null ? def : val);
    }

    /**
     * Get the value of type {@code P} of the value element at the supplied key.
     *
     * @throws ElementException If the value element was not found or the type did not match
     */
    default <P> P getObject(Key key, Class<P> primitiveClass) throws ElementException {
        return getValue(key).valueOrThrow(primitiveClass);
    }

    /**
     * Get the value of type {@code P} of the value element at the supplied key.
     * Returns the supplied default value if the value element was not found or the type did not match.
     */
    default <P> P getObject(Key key, Class<P> primitiveClass, P def) {
        final DataElement el = getOrNull(key);
        if (el == null || !el.isValue()) return def;
        final P val = el.asValue().valueOrNull(primitiveClass);
        return def == null ? val : (val == null ? def : val);
    }

    /**
     * Get the element of type {@code E} at the supplied key.
     * Returns the supplied default element if the element was not found or the type did not match.
     */
    default <E extends DataElement> E getElement(Key key, Class<E> elementClass, E def) {
        var el = getOrNull(key);
        if (el == null) return def;
        return el.isOf(elementClass) ? el.unsafeCast() : def;
    }


    // MAP

    /**
     * Get the map element at the supplied key.
     *
     * @throws ElementException If the map element was not found
     */
    default DataMap getMap(Key key) throws ElementException {
        return get(key).requireOf(DataMap.class);
    }

    /**
     * Get the map element at the supplied key.
     * Returns the supplied default value if the map element was not found.
     */
    default DataMap getMap(Key key, DataMap def) {
        return getElement(key, DataMap.class, def);
    }

    /**
     * Run the supplied action on the map element at the supplied key, or the elseAction if no map element is found.
     */
    default MappedElements<Key> ifMap(Key key, Consumer<DataMap> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isMap()) action.accept(el.asMap());
        else if (elseAction != null) elseAction.run();
        return this;
    }

    /**
     * Run the supplied action on the map element at the supplied key.
     */
    default MappedElements<Key> ifMap(Key key, Consumer<DataMap> action) {
        return ifMap(key, action, null);
    }

    // LIST

    /**
     * Get list map element at the supplied key.
     *
     * @throws ElementException If the list element was not found.
     */
    default DataList getList(Key key) throws ElementException {
        return get(key).requireOf(DataList.class);
    }

    /**
     * Get the list element at the supplied key.
     * Returns the supplied default value if the list element was not found.
     */
    default DataList getList(Key key, DataList def) {
        return getElement(key, DataList.class, def);
    }

    /**
     * Run the supplied action on the list element at the supplied key, or the elseAction if no list element is found.
     */
    default MappedElements<Key> ifList(Key key, Consumer<DataList> action, Runnable elseAction) {
        final DataElement el = getOrNull(key);
        if (el != null && el.isList()) action.accept(el.asList());
        else if (elseAction != null) elseAction.run();
        return this;
    }

    /**
     * Run the supplied action on the list element at the supplied key
     */
    default MappedElements<Key> ifList(Key key, Consumer<DataList> action) {
        return ifList(key, action, null);
    }

    // VALUE GETTERS

    /**
     * Get the Boolean value from the value element at the supplied key.
     *
     * @throws ElementException If the Boolean value element was not found
     */
    default Boolean getBoolean(Key key) throws ElementException {
        return getObject(key, Boolean.class);
    }

    /**
     * Get the Boolean value from the value element at the supplied key.
     * Returns the supplied default if the Boolean value element was not found.
     */
    default Boolean getBoolean(Key key, boolean def) {
        return getObject(key, Boolean.class, def);
    }

    /**
     * Get the Number value from the value element at the supplied key.
     *
     * @throws ElementException If the Number value element was not found
     */
    default Number getNumber(Key key) throws ElementException {
        return getObject(key, Number.class);
    }

    /**
     * Get the Number value from the value element at the supplied key.
     * Returns the supplied default if the Number value element was not found.
     */
    default Number getNumber(Key key, Number def) {
        return getObject(key, Number.class, def);
    }

    /**
     * Get the Integer value from the value element at the supplied key.
     *
     * @throws ElementException If the Integer value element was not found
     */
    default Integer getInt(Key key) throws ElementException {
        return getObject(key, Integer.class);
    }

    /**
     * Get the Integer value from the value element at the supplied key.
     * Returns the supplied default if the Integer value element was not found.
     */
    default Integer getInt(Key key, int def) {
        return getObject(key, Integer.class, def);
    }

    /**
     * Get the Double value from the value element at the supplied key.
     *
     * @throws ElementException If the Double value element was not found
     */
    default Double getDouble(Key key) throws ElementException {
        return getObject(key, Double.class);
    }

    /**
     * Get the Double value from the value element at the supplied key.
     * Returns the supplied default if the Double value element was not found.
     */
    default Double getDouble(Key key, double def) {
        return getObject(key, Double.class, def);
    }

    /**
     * Get the Float value from the value element at the supplied key.
     *
     * @throws ElementException If the Float value element was not found
     */
    default Float getFloat(Key key) throws ElementException {
        return getObject(key, Float.class);
    }

    /**
     * Get the Float value from the value element at the supplied key.
     * Returns the supplied default if the Float value element was not found.
     */
    default Float getFloat(Key key, float def) {
        return getObject(key, Float.class, def);
    }

    /**
     * Get the Long value from the value element at the supplied key.
     *
     * @throws ElementException If the Long value element was not found
     */
    default Long getLong(Key key) throws ElementException {
        return getObject(key, Long.class);
    }

    /**
     * Get the Long value from the value element at the supplied key.
     * Returns the supplied default if the Long value element was not found.
     */
    default Long getLong(Key key, long def) {
        return getObject(key, Long.class, def);
    }

    /**
     * Get the String value from the value element at the supplied key.
     *
     * @throws ElementException If the String value element was not found
     */
    default String getString(Key key) throws ElementException {
        return getObject(key, String.class);
    }

    /**
     * Get the String value from the value element at the supplied key.
     * Returns the supplied default if the String value element was not found.
     */
    default String getString(Key key, String def) {
        return getObject(key, String.class, def);
    }
}
