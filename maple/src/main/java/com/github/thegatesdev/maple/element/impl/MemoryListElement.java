package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementCollection;
import com.github.thegatesdev.maple.element.ListElement;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public final class MemoryListElement implements ListElement {

    private final Element[] values;

    private MemoryListElement(Element[] values) {
        this.values = values;
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
    public ListElement each(Function<Element, Element> transformer) {
        final Element[] output = new Element[values.length];

        for (int i = 0; i < values.length; i++) {
            Element original = values[i];
            output[i] = transformer.apply(original);
        }

        return new MemoryListElement(output);
    }

    @Override
    public ListElement crawl(Function<Element, Element> transformer) {
        final Element[] output = new Element[values.length];

        for (int i = 0; i < values.length; i++) {
            Element value = values[i];
            if (value instanceof ElementCollection collection) {
                value = collection.crawl(transformer);
            }
            output[i] = transformer.apply(value);
        }

        return new MemoryListElement(output);
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
    public ListElement values(Function<Element, Element> transformer) {
        return each(transformer);
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
    public ListElement getList() {
        return this;
    }


    private static final class Builder implements ListElement.Builder {

        private static final Element[] EMPTY_EL_ARR = new Element[0];
        private final List<Element> values;


        private Builder(int initialCapacity) {
            this.values = Collections.synchronizedList(new ArrayList<>(initialCapacity));
        }
        
        private Builder() {
            this(5);
        }


        @Override
        public ListElement build() {
            return new MemoryListElement(values.toArray(EMPTY_EL_ARR));
        }

        @Override
        public void add(Element element) {
            values.add(element);
        }

        @Override
        public void addFrom(ListElement listElement) {
            if (listElement instanceof MemoryListElement memoryListElement)
                addFrom(memoryListElement.values);
            else
                listElement.each((Consumer<Element>) values::add);
        }

        @Override
        public void addFrom(Element[] values) {
            addFrom(Arrays.asList(values));
        }

        @Override
        public void addFrom(Collection<Element> values) {
            this.values.addAll(values);
        }

        @Override
        public Element set(int index, Element element) {
            return values.set(index, element);
        }

        @Override
        public Element remove(int index) {
            return values.remove(index);
        }
    }
}
