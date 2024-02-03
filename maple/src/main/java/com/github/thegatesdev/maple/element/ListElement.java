package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryListElement;

import java.util.Collection;
import java.util.Optional;

public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

    Element get(int index);

    Optional<Element> find(int index);


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


    interface Builder {
        ListElement build();

        void add(Element element);

        void addFrom(ListElement listElement);

        void addFrom(Element[] values);

        void addFrom(Collection<Element> values);

        Optional<Element> set(int index, Element element);

        Optional<Element> remove(int index);
    }
}
