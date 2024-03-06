package com.github.thegatesdev.maple.adapt;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ListElement;

import java.math.BigInteger;
import java.util.*;

/**
 * Adapter for SnakeYAML and SnakeYAML-Engine outputs.
 * <ul>
 * <li>{@code !!binary} tags will be converted to a Base64 string value</li>
 * <li>{@code !!timestamp} tags are not supported</li>
 * <li>{@code !!omap} or {@code !!pairs} tags will be adapted to a dictionary element</li>
 * <li>{@code !!set} will be adapted to a list element</li>
 * </ul>
 */
public final class SnakeYamlAdapter implements Adapter {

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
        if (input instanceof Set<?> val) return parseCollection(val); // !!set
        if (input instanceof List<?> val)
            return tryParseOMAP(val).orElseGet(() -> parseCollection(val)); // !!omap/pairs or !!seq
        if (input instanceof Map<?, ?> val) return parseMap(val);
        // No Java 21 misery... Type pattern matching switch would've been nice...
        throw new IllegalArgumentException("Input of type %s could not be adapted to an element, is this really SnakeYaml-Engine output?".formatted(input.getClass().getSimpleName()));
    }

    private DictElement parseMap(Map<?, ?> input) {
        DictElement.Builder output = DictElement.builder(input.size());
        input.forEach((key, value) -> output.put(key.toString(), adapt(value)));
        return output.build();
    }

    private ListElement parseCollection(Collection<?> input) {
        ListElement.Builder output = ListElement.builder(input.size());
        for (Object value : input) output.add(adapt(value));
        return output.build();
    }

    private Optional<Element> tryParseOMAP(List<?> input) {
        // Parse a list of pairs aka an ordered map (why does this exist? Aren't YAML maps already ordered?).
        // Comes in a List with values of type Object[].
        if (!(input.get(0) instanceof Object[]))
            return Optional.empty(); // Early return so we avoid allocating a dictionary builder.
        DictElement.Builder output = DictElement.builder(input.size());
        for (Object value : input) {
            if (value instanceof Object[] array && array.length == 2)
                output.put(array[0].toString(), adapt(array[1]));
            // This basically already means invalid data has been passed in
            // as there is no other YAML type that converts to an Object[] using SnakeYAML-Engine.
            return Optional.empty();
        }
        return Optional.of(output.build());
    }
}
