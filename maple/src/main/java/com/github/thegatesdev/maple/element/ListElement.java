package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryListElement;

public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

    @Override
    default ElementType type() {
        return ElementType.LIST;
    }
}
