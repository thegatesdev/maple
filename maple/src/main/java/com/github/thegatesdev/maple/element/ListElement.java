package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryListElement;

import java.util.List;
import java.util.Optional;

public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

    Element get(int index);

    Optional<Element> find(int index);

    List<Element> copyBack();


    @Override
    default boolean isList() {
        return true;
    }

    @Override
    default ListElement getList() {
        return this;
    }

    @Override
    default ElementType type() {
        return ElementType.LIST;
    }
}
