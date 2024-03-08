package com.github.thegatesdev.maple.element.impl.internal;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementCollection;
import com.github.thegatesdev.maple.element.ListElement;
import com.github.thegatesdev.maple.exception.ElementKeyNotPresentException;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public final class MemoryDictElement implements DictElement {

    public static final MemoryDictElement EMPTY = new MemoryDictElement(Collections.emptyMap());
    private final Map<String, Element> entries;
    private final int cachedHash;
    private final AtomicReference<ListElement> valuesRef = new AtomicReference<>();

    private MemoryDictElement(Map<String, Element> entries) {
        this.entries = entries;
        this.cachedHash = makeHash();
    }


    public static DictElement of(Map<String, Element> entries) {
        Objects.requireNonNull(entries, "given map is null");

        if (entries.isEmpty()) return EMPTY;
        return new MemoryDictElement(new LinkedHashMap<>(entries));
    }

    public static DictElement.Builder builder() {
        return builder(5);
    }

    public static DictElement.Builder builder(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        return new Builder(initialCapacity);
    }


    @Override
    public Element get(String key) {
        Objects.requireNonNull(key, "given key is null");

        Element value = entries.get(key);
        if (value == null) throw new ElementKeyNotPresentException(key);
        return value;
    }

    @Override
    public Optional<Element> find(String key) {
        Objects.requireNonNull(key, "given key is null");

        return Optional.ofNullable(entries.get(key));
    }

    @Override
    public void each(BiConsumer<String, Element> action) {
        entries.forEach(action);
    }

    @Override
    public Map<String, Element> view() {
        return Collections.unmodifiableMap(entries);
    }

    @Override
    public DictElement.Builder toBuilder() {
        return new Builder(entries);
    }


    @Override
    public void each(Consumer<Element> action) {
        Objects.requireNonNull(action, "given action is null");

        entries.values().forEach(action);
    }

    @Override
    public void crawl(Consumer<Element> action) {
        Objects.requireNonNull(action, "given action is null");

        entries.values().forEach(element -> {
            if (element instanceof ElementCollection collection) {
                collection.crawl(action);
            }
            action.accept(element);
        });
    }

    @Override
    public Stream<Element> stream() {
        return entries.values().stream();
    }

    @Override
    public ListElement values() {
        if (valuesRef.get() == null) synchronized (valuesRef) {
            if (valuesRef.get() == null) valuesRef.set(ListElement.of(entries.values()));
        }
        return valuesRef.get();
    }

    @Override
    public int count() {
        return entries.size();
    }


    @Override
    public String toString() {
        return "map{" + entries.size() + "}";
    }

    @Override
    public int hashCode() {
        return cachedHash;
    }

    private int makeHash() {
        return entries.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof DictElement dictElement)) return false;
        if (this.cachedHash != dictElement.hashCode()) return false;

        if (other instanceof MemoryDictElement memoryDictElement)
            return this.entries.equals(memoryDictElement.entries);
        return this.entries.equals(dictElement.view());
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
            Objects.requireNonNull(key, "given key is null");
            Objects.requireNonNull(key, "given element is null");

            checkEdit();
            values.put(key, element);
            return this;
        }

        @Override
        public DictElement.Builder putAll(DictElement values) {
            Objects.requireNonNull(values, "given dictionary element is null");

            return putAll(values instanceof MemoryDictElement memoryDictElement ? memoryDictElement.entries : values.view());
        }

        @Override
        public DictElement.Builder putAll(Map<String, Element> values) {
            Objects.requireNonNull(values, "given map is null");

            checkEdit();
            this.values.putAll(values);
            return this;
        }

        @Override
        public DictElement.Builder remove(String key) {
            Objects.requireNonNull(key, "given key is null");

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
