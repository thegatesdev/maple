package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementCollection;
import com.github.thegatesdev.maple.element.ListElement;
import com.github.thegatesdev.maple.exception.ElementKeyNotPresentException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class MemoryDictElement implements DictElement {

    public static final MemoryDictElement EMPTY = new MemoryDictElement(Collections.emptyMap());
    private final Map<String, Element> values;
    private final int cachedHash;

    private MemoryDictElement(Map<String, Element> values) {
        this.values = values;
        this.cachedHash = makeHash();
    }


    public static DictElement of(Map<String, Element> values) {
        if (values.isEmpty()) return EMPTY;
        return new MemoryDictElement(new LinkedHashMap<>(values));
    }

    public static DictElement.Builder builder() {
        return builder(5);
    }

    public static DictElement.Builder builder(int initialCapacity) {
        return new Builder(initialCapacity);
    }


    @Override
    public Element get(String key) {
        Objects.requireNonNull(key, "key cannot be null");

        Element value = values.get(key);
        if (value == null) throw new ElementKeyNotPresentException(key);
        return value;
    }

    @Override
    public Optional<Element> find(String key) {
        Objects.requireNonNull(key, "key cannot be null");

        return Optional.ofNullable(values.get(key));
    }

    @Override
    public void each(BiConsumer<String, Element> action) {
        values.forEach(action);
    }

    @Override
    public Map<String, Element> view() {
        return Collections.unmodifiableMap(values);
    }

    @Override
    public DictElement.Builder toBuilder() {
        return new Builder(values);
    }


    @Override
    public void each(Consumer<Element> action) {
        values.values().forEach(action);
    }

    @Override
    public void crawl(Consumer<Element> action) {
        Objects.requireNonNull(action, "key cannot be null");

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
    public String toString() {
        return "map{" + values.size() + "}";
    }

    @Override
    public int hashCode() {
        return cachedHash;
    }

    private int makeHash() {
        return values.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof DictElement dictElement)) return false;
        if (this.cachedHash != dictElement.hashCode()) return false;

        if (other instanceof MemoryDictElement memoryDictElement)
            return this.values.equals(memoryDictElement.values);
        return this.values.equals(dictElement.view());
    }


    public static final class Builder implements DictElement.Builder {

        private boolean needsCopy;
        private Map<String, Element> values;

        private Builder(Map<String, Element> values) {
            this.values = values;
            this.needsCopy = true;
        }

        private Builder(int initialCapacity) {
            this.values = new LinkedHashMap<>(initialCapacity);
            this.needsCopy = false;
        }


        private void checkEdit() {
            if (needsCopy) {
                values = new LinkedHashMap<>(values);
                needsCopy = false;
            }
        }


        @Override
        public DictElement build() {
            if (values.isEmpty()) return DictElement.empty();
            needsCopy = true;
            // Don't defensively copy the builder values map.
            // Often, a builder is only used once and then discarded.
            // If that is not the case, we copy the map when a modification is made.
            return new MemoryDictElement(values);
        }


        @Override
        public DictElement.Builder put(String key, Element element) {
            checkEdit();
            values.put(key, element);
            return this;
        }

        @Override
        public DictElement.Builder putAll(DictElement other) {
            return putAll(other instanceof MemoryDictElement memoryDictElement ? memoryDictElement.values : other.view());
        }

        @Override
        public DictElement.Builder putAll(Map<String, Element> values) {
            checkEdit();
            this.values.putAll(values);
            return this;
        }

        @Override
        public DictElement.Builder remove(String key) {
            checkEdit();
            values.remove(key);
            return this;
        }

        @Override
        public Map<String, Element> view() {
            return Collections.unmodifiableMap(values);
        }
    }
}
