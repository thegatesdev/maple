package io.github.thegatesdev.maple;

import java.util.Collections;
import java.util.List;

public class DataList extends DataElement {

    private final List<DataElement> elements, view;

    DataList(List<DataElement> elements) {
        this.elements = elements;
        this.view = Collections.unmodifiableList(elements);
    }

    // -- BASIC OPERATIONS

    /**
     * Replaces the element at the specified index with the specified element.
     * Also see {@link List#set(int, Object)}.
     *
     * @param index   The index of the element to replace.
     * @param element The element to be put at the index.
     * @return The previous element mapped at this index.
     */
    public DataElement set(int index, DataElement element) {
        var old = elements.set(index, element);
        connectThis(element, index);
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Adds the specified element to the end of the list.
     * Also see {@link List#add(Object)}.
     *
     * @param element The element to add.
     */
    public void add(DataElement element) {
        elements.add(element);
        connectThis(element, size());
    }

    /**
     * Removed the element at this index, shifting any elements after that to the left.
     * Also see {@link List#remove(int)}.
     *
     * @param index The index of the element to remove.
     * @return The removed element.
     */
    public DataElement remove(int index) {
        var old = elements.remove(index);
        if (old != null) old.disconnect();
        return old;
    }

    /**
     * Get the element at the specified index, or null.
     *
     * @param index The index of the element to get.
     * @return The element at the specified index, or {@code null}.
     */
    public DataElement getOrNull(int index) {
        if (index < 0 || index >= elements.size()) return null;
        return elements.get(index);
    }

    /**
     * Get the element at the specified index.
     *
     * @param index The index of the element to get.
     * @return The element at the specified index, or a new {@link DataNull}.
     */
    public DataElement get(int index) {
        var el = getOrNull(index);
        return el == null ? connectThis(new DataNull(), index) : el;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        return elements.size();
    }

    // -- ELEMENT

    private DataElement connectThis(DataElement element, int index) {
        return element.connect(this, "[" + index + "]");
    }

    @Override
    protected Object raw() {
        return elements;
    }

    @Override
    public Object view() {
        return view;
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }
}
