package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryDictElement;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public sealed interface DictElement extends Element, ElementCollection permits MemoryDictElement {


    Element get(String key);

    Optional<Element> find(String key);

    void each(BiConsumer<String, Element> action);

    Map<String, Element> copyBack();


    @Override
    default boolean isDict() {
        return true;
    }

    @Override
    default DictElement getDict() {
        return this;
    }

    @Override
    default ElementType type() {
        return ElementType.DICTIONARY;
    }
}
