package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementCollection;
import com.github.thegatesdev.maple.element.ListElement;
import com.github.thegatesdev.maple.exception.ElementKeyNotPresentException;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class MemoryDictElement implements DictElement {

    private static final MemoryDictElement EMPTY = new MemoryDictElement(Collections.emptyMap());
    private final Map<String, Element> values;

    private MemoryDictElement(Map<String, Element> values) {
        this.values = values;
    }


    public static DictElement of(Map<String, Element> values) {
        if (values.isEmpty()) return EMPTY;
        return new MemoryDictElement(new LinkedHashMap<>(values));
    }


    @Override
    public Element get(String key) {
        Element value = values.get(key);
        if (value == null) throw new ElementKeyNotPresentException(key);
        return value;
    }

    @Override
    public Optional<Element> find(String key) {
        return Optional.ofNullable(values.get(key));
    }

    @Override
    public void each(BiConsumer<String, Element> action) {
        values.forEach(action);
    }

    @Override
    public void each(Consumer<Element> action) {
        values.values().forEach(action);
    }

    @Override
    public void crawl(Consumer<Element> action) {
        values.values().forEach(element -> {
            if (element instanceof ElementCollection collection) {
                collection.crawl(action);
            }
            action.accept(element);
        });
    }

    @Override
    public Stream<Element> stream() {
        return values.values().stream();
    }

    @Override
    public ListElement values() {
        return Element.of(values.values());
    }

    @Override
    public int count() {
        return values.size();
    }

    @Override
    public DictElement getDict() {
        return this;
    }
}
