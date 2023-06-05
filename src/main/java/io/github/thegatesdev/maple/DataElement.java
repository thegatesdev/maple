package io.github.thegatesdev.maple;

import io.github.thegatesdev.maple.exception.ElementException;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * The base class for any data element.
 * A data element is an element in a data structure.
 */
public abstract class DataElement implements Comparable<DataElement> {

    private DataElement parent;
    private String name;
    private String[] path;

    /**
     * Constructs a new DataElement.
     */
    protected DataElement() {
    }

    // -- DATA

    /**
     * Connects this element to the specified parent with the specified name.
     *
     * @param newParent The parent to connect to.
     * @param newName   The name to hold.
     * @return This DataElement.
     */
    protected DataElement connect(DataElement newParent, String newName) {
        if (parent != null) throw new RuntimeException("Parent already set");
        parent = Objects.requireNonNull(newParent, "Parent cannot be null");
        name = Objects.requireNonNull(newName, "Name cannot be null");
        path = null;
        return this;
    }

    /**
     * Disconnect this element from the set parent, if any.
     *
     * @return This DataElement.
     */
    protected DataElement disconnect() {
        parent = null;
        path = null;
        return this;
    }


    /**
     * @return The name of this element, or "root".
     */
    protected String friendlyName() {
        return name == null ? "root" : name;
    }

    /**
     * Gets the parent of this element. Can be {@code null}.
     *
     * @return The parent of this element.
     */
    public DataElement parent() {
        return parent;
    }

    /**
     * @return {@code true} if this element has a non-null parent.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Check if the supplied parent is in the chain of parents of this DataElement.
     *
     * @param parent The parent to check for.
     * @return {@code true} if the parent was found.
     */
    public boolean isChild(DataElement parent) {
        if (this.parent == null) return false;
        if (this.parent == parent) return true;
        return this.parent.isChild(parent);
    }

    /**
     * Gets the amount of parents of this element, or how deeply nested this element is.
     *
     * @return The amount of parents until the root element.
     */
    public int nested() {
        if (!hasParent()) return 0;
        return parent.nested() + 1;
    }

    /**
     * Gets the path of element keys from the root to this element, with the first element being the name of the root, and the last the name of this element.
     *
     * @return The path to this element.
     */
    public String[] path() {
        if (path == null) {
            int nested = nested();
            addPath(path = new String[nested + 1], nested);
        }
        return path;
    }

    private void addPath(String[] collect, int index) {
        collect[index] = friendlyName();
        if (index == 0) return;
        parent.addPath(collect, --index);
    }

    // -- VALUE

    /**
     * Gets the raw value this element is backed by.
     *
     * @return The raw value.
     */
    protected abstract Object raw();

    /**
     * Gets the unmodifiable view of the value this element is backed by.
     *
     * @return The view of the value of this element.
     */
    public abstract Object view();

    /**
     * @return {@code true} if this element does not contain a value. Depends on the implementation.
     */
    public abstract boolean isEmpty();

    /**
     * @return Inverse of {@link DataElement#isEmpty()}.
     */
    public boolean isPresent() {
        return !isEmpty();
    }

    // -- ELEMENT TYPE

    /**
     * Casts this element to {@code E}.
     *
     * @param <E> The type to cast to.
     * @return The cast element.
     * @throws ClassCastException when this element is could not be cast to {@code E}.
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E unsafeCast() throws ClassCastException {
        return (E) this;
    }

    /**
     * Cast this element to E
     *
     * @param <E>          The type to cast this element to.
     * @param elementClass The class to cast this element with.
     * @return The same DataElement as E, or null if this element does not conform to elementClass.
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E castOrNull(Class<E> elementClass) {
        return elementClass.isInstance(this) ? (E) this : null;
    }


    /**
     * Get this element as E, or throw.
     *
     * @param elementClass The class this element is required to be.
     * @param <E>          The type of {@code elementClass}.
     * @return This element, cast to E.
     * @throws ElementException If this element is not assignable to {@code elementClass}.
     */
    @SuppressWarnings("unchecked")
    public <E extends DataElement> E requireOf(Class<E> elementClass) throws ElementException {
        if (!elementClass.isInstance(this)) throw ElementException.requireType(this, elementClass);
        return ((E) this);
    }


    // NULL

    /**
     * Check if this element is a DataNull.
     *
     * @return {@code true} if this element is a DataNull.
     */
    public boolean isNull() {
        return false;
    }

    // MAP

    /**
     * Check if this element is a DataMap.
     *
     * @return {@code true} if this element is a DataMap.
     */
    public boolean isMap() {
        return false;
    }

    /**
     * Get this element as a DataMap, or throw.
     *
     * @return This element as a DataMap.
     * @throws UnsupportedOperationException If this element is not a DataMap.
     */
    public DataMap asMap() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a map!");
    }

    /**
     * Run the mapConsumer if this element is a DataMap.
     *
     * @param mapConsumer The if action.
     */
    public final void ifMap(Consumer<DataMap> mapConsumer) {
        ifMap(mapConsumer, null);
    }

    /**
     * Run the mapConsumer if this element is a DataMap, or the elseAction.
     *
     * @param mapConsumer The if action.
     * @param elseAction  The else action.
     */
    public void ifMap(Consumer<DataMap> mapConsumer, Runnable elseAction) {
        if (elseAction != null) elseAction.run();
    }

    // VALUE

    /**
     * Check if this element is a DataValue.
     *
     * @return {@code true} if this element is a DataValue.
     */
    public boolean isValue() {
        return false;
    }

    /**
     * Get this element as a DataValue, or throw.
     *
     * @return This element as a DataValue.
     * @throws UnsupportedOperationException If this element is not a DataValue.
     */
    public DataValue asValue() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a value!");
    }

    /**
     * Run the valueConsumer if this element is a DataValue.
     *
     * @param valueConsumer The if action.
     */
    public final void ifValue(Consumer<DataValue> valueConsumer) {
        ifValue(valueConsumer, null);
    }

    /**
     * Run the valueConsumer if this element is a DataValue, or the elseAction.
     *
     * @param valueConsumer The if action.
     * @param elseAction    The else action.
     */
    public void ifValue(Consumer<DataValue> valueConsumer, Runnable elseAction) {
        if (elseAction != null) elseAction.run();
    }

    // LIST

    /**
     * Check if this element is a DataList.
     *
     * @return {@code true} if this element is a DataList.
     */
    public boolean isList() {
        return false;
    }

    /**
     * Get this element as a DataList, or throw.
     *
     * @return This element as a DataList.
     * @throws UnsupportedOperationException If this element is not a DataList.
     */
    public DataList asList() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not a list!");
    }

    /**
     * Run the listConsumer if this element is a DataList.
     *
     * @param listConsumer The if action.
     */
    public final void ifList(Consumer<DataList> listConsumer) {
        ifList(listConsumer, null);
    }

    /**
     * Run the listConsumer if this element is a DataList, or the elseAction.
     *
     * @param listConsumer The if action.
     * @param elseAction   The else action.
     */
    public void ifList(Consumer<DataList> listConsumer, Runnable elseAction) {
        if (elseAction != null) elseAction.run();
    }

    // -- OBJECT

    /**
     * Creates a new element of the same type, not copying the optional containing elements.
     *
     * @return The copied element.
     */
    public abstract DataElement shallowCopy();

    /**
     * Creates a new element of the same type, copying all optional containing elements.
     *
     * @return The copied element.
     */
    public abstract DataElement deepCopy();

    /**
     * Compares elements by name.
     */
    @Override
    public int compareTo(DataElement o) {
        return name.compareToIgnoreCase(o.name);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        Object raw = raw();
        result = 31 * result + (raw != null ? raw.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataElement that)) return false;
        return Objects.equals(name, that.name) && raw().equals(that.raw());
    }
}
