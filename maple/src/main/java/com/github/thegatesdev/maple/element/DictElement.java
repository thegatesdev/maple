package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryDictElement;

import java.util.Map;
import java.util.Optional;

public sealed interface DictElement extends Element, ElementCollection permits MemoryDictElement {


    Element get(String key);

    Optional<Element> find(String key);

    Builder modify();


    @Override
    default boolean isDict() {
        return true;
    }

    @Override
    DictElement getDict();

    @Override
    default ElementType type() {
        return ElementType.DICTIONARY;
    }


    interface Builder {
        DictElement build();

        Builder set(String key, Element element);

        Builder addFrom(DictElement dictElement);

        Builder addFrom(Map<String, Element> values);

        Builder remove(String key);
    }
}
