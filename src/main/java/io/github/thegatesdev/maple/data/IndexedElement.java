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
 * An abstract class for indexed elements. Implemented by DataList and DataArray.
 * Has common methods for getting different elements using an index.
 */
public abstract class IndexedElement extends DataElement implements Iterable<DataElement> {

    /**
     * Get the element at this index.
     *
     * @param index The index of the element.
     * @return The element at this index, or a new {@link DataNull}.
     */
    public DataElement get(int index) {
        final DataElement element = getOrNull(index);
        if (element == null) return new DataNull().setData(this, "[" + index + "]");
        return element;
    }

    /**
     * Get the value of the primitive at this index, or throw.
     *
     * @param index          The index of the primitive.
     * @param primitiveClass The class the primitive should be of.
     * @param <P>            The type the primitive should be of.
     * @return The value of the primitive.
     * @throws ElementException If the primitive was not found, or the value did not conform to P.
     */
    public <P> P get(int index, Class<P> primitiveClass) throws ElementException {
        return getPrimitive(index).requireValue(primitiveClass);
    }

    /**
     * Get the primitive element at this index.
     *
     * @param index The index of the primitive.
     * @return The found primitive.
     * @throws ElementException If the element was not found, or the element was not a primitive.
     */
    public DataPrimitive getPrimitive(int index) throws ElementException {
        return get(index).requireOf(DataPrimitive.class);
    }

    /**
     * Get the array element at this index.
     *
     * @param index The index of the array.
     * @return The found DataArray.
     * @throws ElementException If the element was not found, or the element was not a DataArray.
     */
    public DataArray getArray(int index) throws ElementException {
        return get(index).requireOf(DataArray.class);
    }

    /**
     * Get the boolean value from the primitive at this index.
     *
     * @param index The index of the primitive.
     * @return The boolean value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a boolean.
     */
    public boolean getBoolean(int index) throws ElementException {
        return getPrimitive(index).booleanValue();
    }

    /**
     * Get the boolean value from the primitive at this index, or a default.
     *
     * @param index The index of the primitive.
     * @param def   The default value to return if the element is not a primitive, or it's value not a boolean.
     * @return The boolean value of the primitive, or the default value.
     */
    public boolean getBoolean(int index, boolean def) {
        return get(index, Boolean.class, def);
    }

    /**
     * Get the value of the primitive at this index, or a default.
     *
     * @param index          The index of the primitive.
     * @param primitiveCLass The class the primitive should be of.
     * @param def            The default value to return when the element is not present, not a primitive, or the type does not match.
     * @param <P>            The type the primitive should be of.
     * @return The value of the primitive, or the default value.
     */
    public <P> P get(int index, Class<P> primitiveCLass, P def) {
        final DataElement el = getOrNull(index);
        if (el == null || !el.isPrimitive()) return def;
        if (def == null)
            return el.asPrimitive().valueOrNull(primitiveCLass);
        final P val = el.asPrimitive().valueOrNull(primitiveCLass);
        return val == null ? def : val;
    }

    /**
     * Get the double value from the primitive at this index.
     *
     * @param index The index of the element.
     * @return The double value of the primitive.
     * @throws ElementException If the element was not a primitive, or it's value not a number.
     */
    public double getDouble(int index) throws ElementException {
        return getPrimitive(index).doubleValue();
    }

    /**
     * Get the double value from the primitive at this index, or a default.
     *
     * @param index The index of the element.
     * @param def   The value to return if the element is not a primitive, or it's value not a double.
     * @return The double value of the primitive.
     */
    public double getDouble(int index, double def) {
        return get(index, Number.class, def).doubleValue();
    }

    /**
     * Get the float value from the primitive at this index.
     *
     * @param index The index of the element.
     * @return The double value of the primitive.
     * @throws ElementException If the element was not a primitive, or it's value not a float.
     */
    public float getFloat(int index) throws ElementException {
        return getPrimitive(index).floatValue();
    }

    /**
     * Get the float value from the primitive at this index, or a default
     *
     * @param index The index of the element.
     * @param def   The value to return if the element is not a primitive, or it's value not a float.
     * @return The double value of the primitive.
     */
    public float getFloat(int index, float def) {
        return get(index, Number.class, def).floatValue();
    }

    /**
     * Get the int value from the primitive at this index.
     *
     * @param index The index of the element.
     * @return The int value of the primitive.
     * @throws ElementException If the element was not a primitive, or it's value not an int.
     */
    public int getInt(int index) throws ElementException {
        return getPrimitive(index).intValue();
    }

    /**
     * Get the int value from the primitive at this index, or a default
     *
     * @param index The index of the element.
     * @param def   The value to return if the element is not a primitive, or it's value not an int.
     * @return The int value of the primitive.
     */
    public int getInt(int index, int def) {
        return get(index, Number.class, def).intValue();
    }

    /**
     * Get the list element at this index.
     *
     * @param index The index of the key.
     * @return The found DataList.
     * @throws ElementException If the element was not found, or the element was not a DataList.
     */
    public DataList getList(int index) throws ElementException {
        return get(index).requireOf(DataList.class);
    }

    /**
     * Get the long value from the primitive at this index.
     *
     * @param index The index of the element.
     * @return The long value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a number.
     */
    public long getLong(int index) throws ElementException {
        return getPrimitive(index).longValue();
    }

    /**
     * Get the long value from the primitive at this index, or a default.
     *
     * @param index The index of the element.
     * @param def   The value to return if the element is not a primitive, or the value is not a number.
     * @return The long value of the primitive, or the default value.
     */
    public long getLong(int index, long def) {
        return get(index, Number.class, def).longValue();
    }

    /**
     * Get the map element at this index
     *
     * @param index The index of the map.
     * @return The found DataMap.
     * @throws ElementException If the element was not found, or the element was not a DataMap.
     */
    public DataMap getMap(int index) throws ElementException {
        return get(index).requireOf(DataMap.class);
    }

    /**
     * Get the String value from the primitive at this index.
     *
     * @param index The index of the primitive.
     * @return The String value of the primitive.
     * @throws ElementException If the element was not a primitive, or the value was not a String.
     */
    public String getString(int index) throws ElementException {
        return getPrimitive(index).stringValue();
    }

    /**
     * Get the String value from the primitive at this index, or a default.
     *
     * @param index The index of this primitive.
     * @param def   The value to return if the element is not a primitive, or the value is not a String.
     * @return The String value of the primitive, or the default value.
     */
    public String getString(int index, String def) {
        return get(index, String.class, def);
    }

    /**
     * Get the value of the primitive associated with this key, cast to P.
     *
     * @param index The index of the primitive.
     * @param <P>   The type to cast to.
     * @return The cast value.
     * @throws ElementException If the element was not found, or is not a primitive.
     */
    public <P> P getUnsafe(int index) throws ElementException {
        return getPrimitive(index).valueUnsafe();
    }

    /**
     * Get the value of the primitive at this index, cast to P, or a default value.
     * This does not return the default value if the cast throws!
     *
     * @param index The index of the primitive.
     * @param <P>   The type to cast to.
     * @param def   The default value to return if the element is not a primitive.
     * @return The cast value.
     */
    public <P> P getUnsafe(int index, P def) {
        final DataElement el = getOrNull(index);
        if (el == null || !el.isPrimitive()) return def;
        if (def == null) return el.asPrimitive().valueUnsafe();
        final P val = el.asPrimitive().valueUnsafe();
        return val == null ? def : val;
    }

    /**
     * Runs the action if the specified element is present and is a DataArray.
     *
     * @param index  The index of the element.
     * @param action The consumer to run when the element is found.
     */
    public void ifArray(int index, Consumer<DataArray> action) {
        ifArray(index, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataArray, or the elseAction if not.
     *
     * @param index      The index to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataArray.
     */
    public void ifArray(int index, Consumer<DataArray> action, Runnable elseAction) {
        final DataElement el = getOrNull(index);
        if (el != null && el.isArray()) action.accept(el.asArray());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present and is a DataList.
     *
     * @param index  The index to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifList(int index, Consumer<DataList> action) {
        ifList(index, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataList, or the elseAction if not.
     *
     * @param index      The index to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataList.
     */
    public void ifList(int index, Consumer<DataList> action, Runnable elseAction) {
        final DataElement el = getOrNull(index);
        if (el != null && el.isList()) action.accept(el.asList());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present and is a DataMap.
     *
     * @param index  The index to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifMap(int index, Consumer<DataMap> action) {
        ifMap(index, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataMap, or the elseAction if not.
     *
     * @param index      The index to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataMap.
     */
    public void ifMap(int index, Consumer<DataMap> action, Runnable elseAction) {
        final DataElement el = getOrNull(index);
        if (el != null && el.isMap()) action.accept(el.asMap());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present.
     * This will never be a DataNull.
     *
     * @param index  The index to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifPresent(int index, Consumer<DataElement> action) {
        ifPresent(index, action, null);
    }

    /**
     * Runs the action if the specified element is present, or the elseAction if not.
     * This will never be a DataNull.
     *
     * @param index      The index to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present.
     */
    public void ifPresent(int index, Consumer<DataElement> action, Runnable elseAction) {
        final DataElement el = getOrNull(index);
        if (el != null) action.accept(el);
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive.
     *
     * @param index  The index to find the element at.
     * @param action The consumer to run when the element is found.
     */
    public void ifPrimitive(int index, Consumer<DataPrimitive> action) {
        ifPrimitive(index, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive, or the elseAction if not.
     *
     * @param index      The index to find the element at.
     * @param action     The consumer to run when the element is found.
     * @param elseAction The runnable to run when the element is not present or not a DataPrimitive.
     */
    public void ifPrimitive(int index, Consumer<DataPrimitive> action, Runnable elseAction) {
        final DataElement el = getOrNull(index);
        if (el != null && el.isPrimitive()) action.accept(el.asPrimitive());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive and it's value is of the specified type.
     *
     * @param index          The index to find the element at.
     * @param primitiveClass The class the DataPrimitive should be of.
     * @param <P>            The type the DataPrimitive should be of.
     * @param action         The consumer to run when the element is found.
     */
    public <P> void ifPrimitiveOf(int index, Class<P> primitiveClass, Consumer<P> action) {
        ifPrimitiveOf(index, primitiveClass, action, null);
    }

    /**
     * Runs the action if the specified element is present and is a DataPrimitive and it's value is of the specified type, or the elseAction if not.
     *
     * @param index          The index to find the element at.
     * @param primitiveClass The class the DataPrimitive should be of.
     * @param <P>            The type the DataPrimitive should be of.
     * @param elseAction     The runnable to run when the element is not present or is not a DataPrimitive, or it's value is not of the specified type.
     * @param action         The consumer to run when the element is found.
     */
    public <P> void ifPrimitiveOf(int index, Class<P> primitiveClass, Consumer<P> action, Runnable elseAction) {
        final DataElement el = getOrNull(index);
        if (el != null && el.isPrimitive()) {
            final DataPrimitive primitive = el.asPrimitive();
            if (primitive.valueOf(primitiveClass)) {
                action.accept(primitive.valueUnsafe());
                return;
            }
        }
        if (elseAction != null) elseAction.run();
    }

    abstract DataElement getOrNull(int index);

    abstract int size();
}
