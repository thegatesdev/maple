package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryDictElement;

public sealed interface DictElement extends Element, ElementCollection permits MemoryDictElement {

    DictElement merged(DictElement other);

    DictElement flattened();


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
}
