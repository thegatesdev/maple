package com.github.thegatesdev.maple.element;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public sealed interface ElementCollection extends Element permits DictElement, ListElement {

    char DEFAULT_DELIMITER = '.';


    Element get(String path, char delimiter);

    Optional<Element> find(String path, char delimiter);

    default Element get(String path) {
        return get(path, DEFAULT_DELIMITER);
    }

    default Optional<Element> find(String path) {
        return find(path, DEFAULT_DELIMITER);
    }


    void each(Consumer<Element> action);

    void crawl(Consumer<Element> action);

    ElementCollection each(Function<Element, Element> transformer);

    ElementCollection crawl(Function<Element, Element> transformer);


    Stream<Element> stream();

    ListElement values();

    ListElement values(Function<Element, Element> transformer);


    int count();
}
