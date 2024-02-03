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

    private final Map<String, Element> values;

    private MemoryDictElement(Map<String, Element> values) {
        this.values = values;
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
    public void entries(BiConsumer<String, Element> action) {
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
        return null; // TODO
    }

    @Override
    public int count() {
        return values.size();
    }

    @Override
    public DictElement getDict() {
        return this;
    }


    private static final class Builder implements DictElement.Builder {

        private final Map<String, Element> values;


        private Builder(int initialCapacity) {
            this.values = Collections.synchronizedMap(new LinkedHashMap<>(initialCapacity));
        }

        private Builder() {
            this(5);
        }


        @Override
        public DictElement build() {
            return new MemoryDictElement(new LinkedHashMap<>(values));
        }

        @Override
        public DictElement.Builder set(String key, Element element) {
            values.put(key, element);
            return this;
        }

        @Override
        public DictElement.Builder addFrom(DictElement dictElement) {
            if (dictElement instanceof MemoryDictElement memoryDictElement)
                addFrom(memoryDictElement.values);
            else
                dictElement.entries(values::put);
            return this;
        }

        @Override
        public DictElement.Builder addFrom(Map<String, Element> values) {
            this.values.putAll(values);
            return this;
        }

        @Override
        public DictElement.Builder remove(String key) {
            values.remove(key);
            return this;
        }
    }
}
