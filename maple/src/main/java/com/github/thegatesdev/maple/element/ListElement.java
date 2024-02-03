package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.MemoryListElement;

import java.util.Collection;
import java.util.Optional;

public sealed interface ListElement extends Element, ElementCollection permits MemoryListElement {

    Element get(int index);

    Optional<Element> find(int index);

    Builder modify();


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

        Builder add(Element element);

        Builder addFrom(ListElement listElement);

        Builder addFrom(Element[] values);

        Builder addFrom(Collection<Element> values);

        Builder set(int index, Element element);

        Builder remove(int index);
    }
}
