package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.exception.ElementKeyNotPresentException;

import java.util.*;

/**
 * A layout parsing a dictionary element to another dictionary element using the given options.
 * An option tries to parse a layout at a specific key in the dictionary.
 * Options can either be <i>required</i>; when the value is not present, an exception will be thrown,
 * otherwise they are <i>optional</i>; when the value is not present, a given default will take its place.
 */
public final class DictLayout implements Layout<DictElement> {

    private final Option[] options;

    private DictLayout(Option[] options) {
        this.options = options;
    }


    @Override
    public DictElement parse(Element value) {
        DictElement input = value.getDict();
        Map<String, Element> output = new LinkedHashMap<>(options.length);

        for (Option option : options) {
            Optional<Element> entry = input.find(option.key);
            if (entry.isPresent()) { // Present
                output.put(option.key, option.layout.parse(entry.get()));
            } else if (option.defaultValue == null)
                throw new ElementKeyNotPresentException(option.key); // Not present, no default
            else output.put(option.key, option.defaultValue); // Not present, default
        }

        return Element.of(output);
    }


    /**
     * Builder for creating a dictionary layout.
     */
    public static final class Builder {
        private static final Layout<Element> IDENTITY_LAYOUT = value -> value;

        private final List<Option> options = new ArrayList<>(5);
        private final Set<String> uniqueKeys = new HashSet<>(5);

        Builder() {
        }

        /**
         * Create the dictionary layout from the options in the builder.
         *
         * @return the new dictionary layout
         */
        public DictLayout build() {
            return new DictLayout(options.toArray(new Option[0]));
        }


        /**
         * Require a value at the given key, parsed with the given layout.
         *
         * @param key    the required key
         * @param layout the parsed layout
         * @return this builder
         */
        public Builder require(String key, Layout<? extends Element> layout) {
            return add(key, layout, null);
        }

        /**
         * Require a value at the given key.
         *
         * @param key the required key
         * @return this builder
         */
        public Builder require(String key) {
            return require(key, IDENTITY_LAYOUT);
        }

        /**
         * Parse the value at the given key with the given layout, if it is present.
         * Otherwise, place the given default value at that key.
         *
         * @param key          the key
         * @param defaultValue the default value
         * @param layout       the layout to parse
         * @return this builder
         */
        public Builder optional(String key, Element defaultValue, Layout<? extends Element> layout) {
            return add(key, layout, Objects.requireNonNull(defaultValue, "defaultValue cannot be null"));
        }

        /**
         * Keep the value at the given key if it is present.
         * Otherwise, place the given default value at that key.
         *
         * @param key          the key
         * @param defaultValue the default value
         * @return this builder
         */
        public Builder optional(String key, Element defaultValue) {
            return optional(key, defaultValue, IDENTITY_LAYOUT);
        }


        private Builder add(String key, Layout<? extends Element> layout, Element defaultValue) {
            if (!uniqueKeys.add(key)) throw new IllegalArgumentException("Duplicate option key '%s'".formatted(key));
            options.add(new Option(key, layout, defaultValue));
            return this;
        }
    }

    private record Option(String key, Layout<? extends Element> layout, Element defaultValue) {
    }
}
