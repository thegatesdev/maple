package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryDictElement;

import java.util.Map;
import java.util.Optional;

public sealed interface DictElement extends Element, ElementCollection permits MemoryDictElement {


    Element get(String key);

    Optional<Element> find(String key);


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

        Element set(String key, Element element);

        void addFrom(DictElement dictElement);

        void addFrom(Map<String, Element> values);

        Element remove(String key);
    }
}
