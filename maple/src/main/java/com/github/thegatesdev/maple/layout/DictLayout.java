package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.exception.ElementException;
import com.github.thegatesdev.maple.exception.ElementKeyNotPresentException;

import java.util.*;

/**
 * A layout applying multiple layouts to specific keys in a dictionary element,
 * optionally providing default values.
 * When a value is not present, <i>required</i> entries will throw,
 * and <i>optional</i> entries will place a default value in that location.
 */
public final class DictLayout implements Layout<DictElement> {

    private final Entry[] entries;

    private DictLayout(Entry[] entries) {
        this.entries = entries;
    }


    /**
     * @throws ElementKeyNotPresentException if an entry is not present and does not have a default value specified
     */
    @Override
    public Optional<DictElement> apply(Element element) throws ElementException {
        DictElement input = element.getDict();
        DictElement.Builder output = input.toBuilder();

        for (Entry entry : entries) {
            Optional<Element> encounter = input.find(entry.key);
            if (encounter.isPresent()) { // Present
                Optional<? extends Element> replacement = entry.layout.apply(encounter.get());
                replacement.ifPresent(value -> output.put(entry.key, value));
            } else if (entry.defaultValue == null) // Not present, required
                throw new ElementKeyNotPresentException(entry.key);
            else output.put(entry.key, entry.defaultValue); // Not present, optional
        }

        return Optional.of(output.build());
    }


    private record Entry(String key, Layout<?> layout, Element defaultValue) {
    }

    /**
     * Builder for creating dictionary layouts.
     */
    public static final class Builder {

        private final List<Entry> entries = new ArrayList<>(5);
        private final Set<String> uniqueKeys = new HashSet<>(5);

        Builder() {
        }

        /**
         * Create a dictionary layout from the entries in this builder.
         *
         * @return the new dictionary layout
         */
        public Layout<DictElement> build() {
            return new DictLayout(entries.toArray(new Entry[0]));
        }


        /**
         * Require a value at the given key, with the given layout applied to it.
         *
         * @param key    the key for the value
         * @param layout the layout to apply
         * @return this builder
         */
        public Builder required(String key, Layout<?> layout) {
            return add(key, layout, null);
        }

        /**
         * Apply the given layout to the element at the given key,
         * or insert the given default value if it is not present.
         *
         * @param key          the key for the value
         * @param layout       the layout to apply
         * @param defaultValue the default value to insert
         * @param <E>          the expected element type
         * @return this builder
         */
        public <E extends Element> Builder optional(String key, Layout<E> layout, E defaultValue) {
            return add(key, layout, Objects.requireNonNull(defaultValue, "defaultValue cannot be null"));
        }


        private <E extends Element> Builder add(String key, Layout<E> layout, E defaultValue) {
            Objects.requireNonNull(key, "key cannot be null");
            Objects.requireNonNull(layout, "layout cannot be null");

            if (!uniqueKeys.add(key)) throw new IllegalArgumentException("Duplicate option key '%s'".formatted(key));
            entries.add(new Entry(key, layout, defaultValue));
            return this;
        }
    }
}
