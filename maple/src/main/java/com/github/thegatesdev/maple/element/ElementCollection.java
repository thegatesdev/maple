package com.github.thegatesdev.maple.element;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public sealed interface ElementCollection extends Element permits DictElement, ListElement {

    void each(Consumer<Element> action);

    void crawl(Consumer<Element> action);

    ElementCollection each(Function<Element, Element> transformer);

    ElementCollection crawl(Function<Element, Element> transformer);


    Stream<Element> stream();

    ListElement values();

    ListElement values(Function<Element, Element> transformer);


    int count();
}
