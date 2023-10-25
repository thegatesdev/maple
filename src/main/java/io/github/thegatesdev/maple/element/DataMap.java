package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.ElementType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class DataMap implements DataElement, DataDictionary<String> {

    private final Map<String, DataElement> elementMap;

    private DataMap(Map<String, DataElement> elementMap) {
        this.elementMap = elementMap;
    }

    public DataMap() {
        this(new LinkedHashMap<>());
    }

    public DataMap(int initialCapacity) {
        this(new LinkedHashMap<>(initialCapacity));
    }

    // Self

    @Override
    public DataMap structureCopy() {
        var output = new LinkedHashMap<String, DataElement>(elementMap.size());
        elementMap.forEach((key, element) ->
                output.put(key, element.structureCopy()));
        return new DataMap(output);
    }

    // Operations

    @Override
    public DataElement getOrNull(String key) {
        return elementMap.get(key);
    }

    @Override
    public void set(String key, DataElement element) {
        elementMap.put(key, element);
    }

    @Override
    public void each(Consumer<DataElement> elementConsumer) {
        elementMap.values().forEach(elementConsumer);
    }

    // Information

    @Override
    public int size() {
        return elementMap.size();
    }

    // Value

    @Override
    public boolean isEmpty() {
        return elementMap.isEmpty();
    }

    @Override
    public Map<String, DataElement> getValue() {
        return elementMap;
    }

    // Type

    @Override
    public ElementType getType() {
        return ElementType.MAP;
    }
}