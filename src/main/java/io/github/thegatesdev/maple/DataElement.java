package io.github.thegatesdev.maple;

import java.util.Objects;

/**
 * The base class for any data element.
 * A data element is an element in a data structure.
 */
public abstract class DataElement implements Comparable<DataElement> {

    private DataElement parent;
    private String name;
    private String[] path;

    protected DataElement() {
    }

    // -- DATA

    protected DataElement connect(DataElement newParent, String newName) {
        if (parent != null) throw new RuntimeException("Parent already set");
        parent = Objects.requireNonNull(newParent, "Parent cannot be null");
        name = Objects.requireNonNull(newName, "Name cannot be null");
        path = null;
        return this;
    }

    protected DataElement disconnect() {
        parent = null;
        name = null;
        path = null;
        return this;
    }


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
     * Check if this element is a DataNull.
     *
     * @return True if this element is a DataNull.
     */
    public boolean isNull() {
        return false;
    }

    // -- OBJECT

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
