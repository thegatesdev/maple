package io.github.thegatesdev.maple.data;

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

import io.github.thegatesdev.maple.exception.ElementException;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * The base class for any data element.
 * A data element is an element in a data structure.
 */
public abstract class DataElement implements Comparable<DataElement> {

    private DataElement parent;
    private String key;

    protected DataElement connect(DataElement newParent, String newKey) {
        if (parent != null) throw new RuntimeException("Parent already set");
        parent = Objects.requireNonNull(newParent, "Parent cannot be null");
        key = Objects.requireNonNull(newKey, "Name cannot be null");
        return this;
    }

    protected final DataElement disconnect() {
        parent = null;
        key = null;
        return this;
    }


    /**
     * @return The parent of this element. Can be {@code null}
     */
    public DataElement parent() {
        return parent;
    }

    /**
     * @return {@code true} if this element has a parent set
     */
    public boolean hasParent() {
        return parent() != null;
    }

    /**
     * Check if this element is a descendant of the provided parent.
     *
     * @param parent The parent to look for
     * @return {@code true} if the parent was found
     */
    public boolean isDescendant(DataElement parent) {
        var ourParent = parent();
        if (ourParent == null) return false;
        if (ourParent == parent) return true;
        return ourParent.isDescendant(parent);
    }


    /**
     * @return The key of this element. Can be {@code null}
     */
    public String key() {
        return key;
    }

    /**
     * @return The friendly name for this element. Will never be null
     */
    public String friendlyKey() {
        var key = key();
        return key == null ? "root" : key;
    }

    /**
     * Gets the path of element keys from the root to this element, with the first element being the name of the root, and the last the name of this element.
     *
     * @return The path to this element.
     */
    public final String[] path() {
        int nested = nested();
        var path = new String[nested + 1];
        addPath(path, nested);
        return path;
    }

    private void addPath(String[] collect, int index) {
        collect[index] = friendlyKey();
        if (index == 0) return;
        parent.addPath(collect, --index);
    }

    /**
     * Returns the amount of parents this element has until the root is reached,
     * or how deeply nested this element is in the structure.
     * A 0 indicates that this element is the root element.
     */
    public int nested() {
        var ourParent = parent();
        if (ourParent == null) return 0;
        return ourParent.nested() + 1;
    }

    // -- VALUE

    /**
     * @return The view of the Object this element is backed by
     */
    public abstract Object view();

    /**
     * @return {@code true} if this element does not contain any value. Depends on the implementation
     */
    public abstract boolean isEmpty();

    /**
     * @return Inverse of {@link #isEmpty()}
     */
    public boolean isPresent() {
        return !isEmpty();
    }

    // -- SELF

    /**
     * Copy this element to a new element of the same type, copying all the containing values.
     *
     * @return The copied element
     */
    public abstract DataElement copy();

    /**
     * Compares elements by name.
     */
    @Override
    public int compareTo(DataElement o) {
        return friendlyKey().compareToIgnoreCase(o.friendlyKey());
    }

    /**
     * @param elementClass The class to check for
     * @return {@code true} if this element is an instance of the type represented by the supplied class
     */
    public boolean isOf(Class<? extends DataElement> elementClass) {
        return elementClass.isInstance(this);
    }


    /**
     * Unsafely cast this element to {@code E}.
     *
     * @return The cast element
     * @throws ClassCastException If the cast fails
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E unsafeCast() throws ClassCastException {
        return (E) this;
    }

    /**
     * Cast this element to {@code E}, or return {@code null}.
     *
     * @return The cast element, or {@code null} if it could not be cast.
     */
    public <E extends DataElement> E castOrNull(Class<E> elementClass) {
        return isOf(elementClass) ? unsafeCast() : null;
    }

    /**
     * Cast this element to {@code E}, or throw an ElementException.
     *
     * @return The cast element
     * @throws ElementException If the element could not be cast
     */
    public <E extends DataElement> E requireOf(Class<E> elementClass) throws ElementException {
        if (!isOf(elementClass)) throw ElementException.requireType(this, elementClass);
        return unsafeCast();
    }


    /**
     * @return {@code true} is this element is a DataNull
     */
    public boolean isNull() {
        return false;
    }

    /**
     * @return {@code true} is this element is a DataMap
     */
    public boolean isMap() {
        return false;
    }

    /**
     * @return {@code true} is this element is a DataList
     */
    public boolean isList() {
        return false;
    }

    /**
     * @return {@code true} is this element is a DataValue
     */
    public boolean isValue() {
        return false;
    }


    /**
     * Get this element as a DataMap, or throw.
     *
     * @return This element as a DataMap.
     * @throws UnsupportedOperationException If this element is not a DataMap.
     */
    public DataMap asMap() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a DataMap!");
    }

    /**
     * Get this element as a DataList, or throw.
     *
     * @return This element as a DataList.
     * @throws UnsupportedOperationException If this element is not a DataList.
     */
    public DataList asList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a DataList!");
    }

    /**
     * Get this element as a DataValue, or throw.
     *
     * @return This element as a DataValue.
     * @throws UnsupportedOperationException If this element is not a DataValue.
     */
    public DataValue<?> asValue() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a DataValue!");
    }


    /**
     * Run the ifAction if this element is a DataMap.
     *
     * @param ifAction The action to run
     */
    public void ifMap(Consumer<DataMap> ifAction) {
        ifMap(ifAction, null);
    }

    /**
     * Run the ifAction if this element is a DataMap, if not run the elseAction.
     *
     * @param ifAction   The if action.
     * @param elseAction The else action.
     */
    public void ifMap(Consumer<DataMap> ifAction, Runnable elseAction) {
        if (isMap()) ifAction.accept(unsafeCast());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Run the ifAction if this element is a DataList.
     *
     * @param ifAction The action to run
     */
    public void ifList(Consumer<DataList> ifAction) {
        ifList(ifAction, null);
    }

    /**
     * Run the ifAction if this element is a DataList, if not run the elseAction.
     *
     * @param ifAction   The if action.
     * @param elseAction The else action.
     */
    public void ifList(Consumer<DataList> ifAction, Runnable elseAction) {
        if (isList()) ifAction.accept(unsafeCast());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Run the ifAction if this element is a DataValue.
     *
     * @param ifAction The action to run
     */
    public void ifValue(Consumer<DataValue<?>> ifAction) {
        ifValue(ifAction, null);
    }

    /**
     * Run the ifAction if this element is a DataValue, if not run the elseAction.
     *
     * @param ifAction   The if action.
     * @param elseAction The else action.
     */
    public void ifValue(Consumer<DataValue<?>> ifAction, Runnable elseAction) {
        if (isValue()) ifAction.accept(unsafeCast());
        else if (elseAction != null) elseAction.run();
    }
}
