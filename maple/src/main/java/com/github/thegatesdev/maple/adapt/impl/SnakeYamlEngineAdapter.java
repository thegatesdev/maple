package com.github.thegatesdev.maple.adapt.impl;

import com.github.thegatesdev.maple.adapt.Adapter;
import com.github.thegatesdev.maple.adapt.OutputType;
import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ListElement;

import java.math.BigInteger;
import java.util.*;

public enum SnakeYamlEngineAdapter implements Adapter {
    INSTANCE;

    @Override
    public DictElement adapt(Map<?, ?> values) {
        DictElement.Builder output = DictElement.builder(values.size());
        values.forEach((key, value) -> output.put(key.toString(), adapt(value)));
        return output.build();
    }

    @Override
    public ListElement adapt(Collection<?> values) {
        ListElement.Builder output = ListElement.builder(values.size());
        for (Object value : values) output.add(adapt(value));
        return output.build();
    }

    @Override
    public Element adapt(Object input) {
        if (input == null) return Element.unset(); // !!null
        if (input instanceof Boolean val) return Element.of(val); // !!bool
        if (input instanceof Integer val) return Element.of(val); // !!int
        if (input instanceof Long val) return Element.of(val); // !!int
        if (input instanceof BigInteger val) return Element.of(val.longValue()); // !!int
        if (input instanceof Double val) return Element.of(val); // !!float
        // SnakeYAML-Engine does not parse to the Float type
        if (input instanceof String val) return Element.of(val); // !!str
        if (input instanceof byte[] val) return Element.of(Base64.getEncoder().encodeToString(val)); // !!binary
        if (input instanceof Set<?> val) return adapt(val); // !!set
        if (input instanceof List<?> val) return tryParseOMAP(val).orElse(adapt(val)); // !!omap/pairs or !!seq
        if (input instanceof Map<?, ?> val) return adapt(val);
        // No Java 21 misery... Type pattern matching switch would've been nice...
        throw new IllegalArgumentException("Input of type %s could not be adapted to an element, is this really SnakeYaml-Engine output?".formatted(input.getClass()));
    }

    @Override
    public OutputType outputType() {
        return OutputType.SNAKEYAML_ENGINE;
    }


    private Optional<Element> tryParseOMAP(List<?> values) {
        // Parse a list of pairs aka an ordered map (why does this exist? Aren't YAML maps already ordered?).
        // Comes in a List with values of type Object[].
        if (!(values.get(0) instanceof Object[]))
            return Optional.empty(); // Early return so we avoid allocating a dictionary builder.
        DictElement.Builder output = DictElement.builder(values.size());
        for (Object value : values) {
            if (!(value instanceof Object[] array) || array.length != 2) {
                // This basically already means invalid data has been passed in
                // as there is no other YAML type that converts to an Object[] using SnakeYAML-Engine.
                return Optional.empty();
            }
            output.put(array[0].toString(), adapt(array[1]));
        }
        return Optional.of(output.build());
    }
}
