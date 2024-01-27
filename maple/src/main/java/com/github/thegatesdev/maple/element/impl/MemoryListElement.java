package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementCollection;
import com.github.thegatesdev.maple.element.ListElement;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public final class MemoryListElement implements ListElement {

    private final Element[] values;

    private MemoryListElement(Element[] values) {
        this.values = values;
    }


    // TODO Implement some efficient pathing method that only has to split once


    @Override
    public Element get(String path, char delimiter) {
        return null;
    }

    @Override
    public Optional<Element> find(String path, char delimiter) {
        return Optional.empty();
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
}
