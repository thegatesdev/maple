package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryDictElement;

public sealed interface DictElement extends Element, ElementCollection permits MemoryDictElement {

    @Override
    default ElementType type() {
        return ElementType.DICTIONARY;
    }
}
