package com.github.thegatesdev.maple.element.impl;

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
        if (values.length == 0) return EMPTY;
        return new MemoryListElement(Arrays.copyOf(values, values.length));
    }

    public static ListElement of(Collection<Element> values) {
        if (values.isEmpty()) return EMPTY;
        return new MemoryListElement(values.toArray(EMPTY_EL_ARR));
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
    public List<Element> copyBack() {
        return new ArrayList<>(Arrays.asList(values));
    }

    @Override
    public Element[] toArray() {
        return Arrays.copyOf(values, values.length);
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

        if (other instanceof MemoryListElement memoryListElement)
            return Arrays.equals(this.values, memoryListElement.values);
        return Arrays.equals(this.values, listElement.toArray());
    }
}
