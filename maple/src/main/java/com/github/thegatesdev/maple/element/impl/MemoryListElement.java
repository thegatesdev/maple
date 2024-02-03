package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementCollection;
import com.github.thegatesdev.maple.element.ListElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class MemoryListElement implements ListElement {

    private static final MemoryListElement EMPTY = new MemoryListElement(Builder.EMPTY_EL_ARR);
    private final Element[] values;

    private MemoryListElement(Element[] values) {
        this.values = values;
    }


    public static ListElement.Builder builder(int initialCapacity) {
        return new Builder(initialCapacity);
    }

    public static ListElement.Builder builder() {
        return new Builder();
    }


    @Override
    public void each(Consumer<Element> action) {
        for (Element value : values) action.accept(value);
    }

    @Override
    public void crawl(Consumer<Element> action) {
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
    public Element get(int index) {
        return values[index];
    }

    @Override
    public Optional<Element> find(int index) {
        if (index < 0 || index >= values.length) return Optional.empty();
        return Optional.of(get(index));
    }

    @Override
    public ListElement.Builder modify() {
        return new Builder(this);
    }

    @Override
    public ListElement getList() {
        return this;
    }


    private static final class Builder implements ListElement.Builder {

        private static final Element[] EMPTY_EL_ARR = new Element[0];
        private final List<Element> values;


        private Builder(MemoryListElement listElement) {
            this.values = Collections.synchronizedList(new ArrayList<>(Arrays.asList(listElement.values)));
        }

        private Builder(int initialCapacity) {
            this.values = Collections.synchronizedList(new ArrayList<>(initialCapacity));
        }

        private Builder() {
            this(5);
        }


        @Override
        public ListElement build() {
            if (values.isEmpty()) return EMPTY;
            return new MemoryListElement(values.toArray(EMPTY_EL_ARR));
        }

        @Override
        public ListElement.Builder add(Element element) {
            values.add(element);
            return this;
        }

        @Override
        public ListElement.Builder addFrom(ListElement listElement) {
            if (listElement instanceof MemoryListElement memoryListElement)
                addFrom(memoryListElement.values);
            else
                listElement.each(values::add);
            return this;
        }

        @Override
        public ListElement.Builder addFrom(Element[] values) {
            return addFrom(Arrays.asList(values));
        }

        @Override
        public ListElement.Builder addFrom(Collection<Element> values) {
            this.values.addAll(values);
            return this;
        }

        @Override
        public ListElement.Builder set(int index, Element element) {
            values.set(index, element);
            return this;
        }

        @Override
        public ListElement.Builder remove(int index) {
            values.remove(index);
            return this;
        }
    }
}
