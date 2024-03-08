package com.github.thegatesdev.maple.element.impl.internal;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementCollection;
import com.github.thegatesdev.maple.element.ListElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class MemoryListElement implements ListElement {

    private static final Element[] EMPTY_EL_ARR = new Element[0];
    public static final MemoryListElement EMPTY = new MemoryListElement(EMPTY_EL_ARR);
    private final Element[] values;
    private final int cachedHash;

    private MemoryListElement(Element[] values) {
        this.values = values;
        this.cachedHash = makeHash();
    }


    public static ListElement of(Element[] values) {
        Objects.requireNonNull(values, "given array is null");

        if (values.length == 0) return EMPTY;
        return new MemoryListElement(Arrays.copyOf(values, values.length));
    }

    public static ListElement of(Collection<Element> values) {
        Objects.requireNonNull(values, "given collection is null");

        if (values.isEmpty()) return EMPTY;
        return new MemoryListElement(values.toArray(EMPTY_EL_ARR)); // Look mom! I'm reusing the empty array! Memory efficiency!
    }

    public static Builder builder() {
        return builder(5);
    }

    public static Builder builder(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        return new Builder(initialCapacity);
    }


    @Override
    public Element get(int index) {
        return values[index];
    }

    @Override
    public Optional<Element> find(int index) {
        if (index < 0 || index >= values.length) return Optional.empty();
        return Optional.of(get(index));
    }

    @Override
    public List<Element> view() {
        return List.of(values);
    }

    @Override
    public ListElement.Builder toBuilder() {
        return new Builder(Arrays.asList(values));
    }

    @Override
    public Element[] toArray() {
        return Arrays.copyOf(values, values.length);
    }


    @Override
    public void each(Consumer<Element> action) {
        Objects.requireNonNull(action, "given action is null");

        for (Element value : values) action.accept(value);
    }

    @Override
    public void crawl(Consumer<Element> action) {
        Objects.requireNonNull(action, "given action is null");

        for (Element value : values) {
            if (value instanceof ElementCollection collection) {
                collection.crawl(action);
            }
            action.accept(value);
        }
    }

    @Override
    public Stream<Element> stream() {
        return Arrays.stream(values);
    }

    @Override
    public ListElement values() {
        return this;
    }

    @Override
    public int count() {
        return values.length;
    }


    @Override
    public String toString() {
        return "list[" + values.length + "]";
    }

    @Override
    public int hashCode() {
        return cachedHash;
    }

    private int makeHash() {
        return Arrays.hashCode(values);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof ListElement listElement)) return false;
        if (this.cachedHash != listElement.hashCode()) return false;
        // If this element is used as a key in some hash based structure (e.g. HashSet or HashMap) this is a duplicate check.
        // It is however more likely that the equals method is used as-is,
        // in which case it's reasonable to check it here, and prevent iterating all values.

        if (other instanceof MemoryListElement memoryListElement)
            return Arrays.equals(this.values, memoryListElement.values);
        return Arrays.equals(this.values, listElement.toArray());
    }


    public static final class Builder implements ListElement.Builder {

        private final List<Element> values;

        private Builder(List<Element> values) {
            this.values = values;
        }

        private Builder(int initialCapacity) {
            this(new ArrayList<>(initialCapacity));
        }


        @Override
        public ListElement build() {
            if (values.isEmpty()) return ListElement.empty();
            return new MemoryListElement(values.toArray(EMPTY_EL_ARR));
        }


        @Override
        public ListElement.Builder add(Element element) {
            Objects.requireNonNull(element, "given element is null");

            values.add(element);
            return this;
        }

        @Override
        public ListElement.Builder addAll(ListElement element) {
            Objects.requireNonNull(element, "given list element is null");

            if (element instanceof MemoryListElement memoryListElement)
                return addAll(memoryListElement.values); // Using the immutable view may be more expensive.
            return addAll(element.view());
        }

        @Override
        public ListElement.Builder addAll(List<Element> elements) {
            Objects.requireNonNull(elements, "given element list is null");

            values.addAll(elements);
            return this;
        }

        @Override
        public ListElement.Builder addAll(Element[] elements) {
            Objects.requireNonNull(elements, "given array is null");

            return addAll(Arrays.asList(elements));
        }

        @Override
        public ListElement.Builder remove(int index) {
            values.remove(index);
            return this;
        }

        @Override
        public ListElement.Builder remove(Element element) {
            Objects.requireNonNull(element, "given element is null");

            values.remove(element);
            return this;
        }

        @Override
        public List<Element> view() {
            return Collections.unmodifiableList(values);
        }
    }
}
