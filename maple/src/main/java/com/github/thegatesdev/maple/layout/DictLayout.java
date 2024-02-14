package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.exception.ElementKeyNotPresentException;

import java.util.*;

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


    public static final class Builder {
        private final List<Option> options = new ArrayList<>(5);
        private final Set<String> uniqueKeys = new HashSet<>(5);

        Builder() {
        }

        public DictLayout build() {
            return new DictLayout(options.toArray(new Option[0]));
        }


        public Builder require(String key, Layout<? extends Element> layout) {
            return add(key, layout, null);
        }

        public Builder require(String key) {
            return require(key, value -> value);
        }

        public Builder optional(String key, Element defaultValue, Layout<? extends Element> layout) {
            return add(key, layout, Objects.requireNonNull(defaultValue, "defaultValue cannot be null"));
        }

        public Builder optional(String key, Element defaultValue) {
            return optional(key, defaultValue, value -> value);
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
