package io.github.thegatesdev.maple.element.impl.internal;

import io.github.thegatesdev.maple.annotation.internal.*;
import io.github.thegatesdev.maple.element.*;
import io.github.thegatesdev.maple.io.*;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * @author Timar Karels
 */
@ValueClassCandidate
public final class MemoryListElement implements ListElement {

    private static final Element[] EMPTY_EL_ARR = new Element[0];
    public static final MemoryListElement EMPTY = new MemoryListElement(EMPTY_EL_ARR);

    private final Element[] values;
    private final int cachedHash;


    MemoryListElement(Element[] values) {
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
    public boolean isEmpty() {
        return this == EMPTY || values.length == 0;
    }


    @Override
    public void writeTo(Destination destination) {
        destination.openArray();
        for (Element value : values) destination.value(value);
        destination.closeArray();
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
    public boolean contentEquals(ListElement other) {
        if (equals(other)) return true;
        return Arrays.equals(values, other.toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemoryListElement that = (MemoryListElement) o;
        return cachedHash == that.cachedHash && Arrays.equals(values, that.values);
    }


    @ValueClassCandidate
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

/*
Copyright 2024 Timar Karels

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
