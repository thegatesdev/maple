package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryListElement;

import java.util.Optional;
import java.util.function.Function;

public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

    Element get(int index);

    Optional<Element> find(int index);


    ListElement merged(ListElement other);


    @Override
    ListElement each(Function<Element, Element> transformer);

    @Override
    ListElement crawl(Function<Element, Element> transformer);

    @Override
    default boolean isList() {
        return true;
    }

    @Override
    ListElement getList();

    @Override
    default ElementType type() {
        return ElementType.LIST;
    }
}
