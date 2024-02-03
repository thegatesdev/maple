package com.github.thegatesdev.maple.element;

import java.util.function.Consumer;
import java.util.stream.Stream;

public sealed interface ElementCollection extends Element permits DictElement, ListElement {

    void each(Consumer<Element> action);

    void crawl(Consumer<Element> action);


    Stream<Element> stream();

    ListElement values();


    int count();
}
