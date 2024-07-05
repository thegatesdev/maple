package com.github.thegatesdev.maple;

import io.github.thegatesdev.maple.maple.element.*;
import io.github.thegatesdev.maple.maple.exception.*;
import org.junit.jupiter.api.*;

import java.util.concurrent.atomic.*;
import java.util.function.*;

final class CollectionOperationsTest {

    private static final DictElement dictElement = DictElement.builder(3)
        .put("int", Element.of(30))
        .put("string", Element.of("foo"))
        .put("nested", DictElement.builder(2)
            .put("unset", Element.none())
            .put("bool", Element.of(true))
            .build())
        .build();

    private static final ListElement listElement = ListElement.builder(3)
        .add(Element.of(30))
        .add(Element.of("foo"))
        .add(DictElement.builder(2)
            .put("unset", Element.none())
            .put("bool", Element.of(true))
            .build())
        .build();

    @Test
    void whenDictElementPresent_thenGet() {
        Assertions.assertEquals(dictElement.get("int"), Element.of(30));
        Assertions.assertEquals(dictElement.get("string"), Element.of("foo"));
        Assertions.assertTrue(dictElement.get("nested").isDict());
    }

    @Test
    void whenListElementPresent_thenGet() {
        Assertions.assertEquals(listElement.get(0), Element.of(30));
        Assertions.assertEquals(listElement.get(1), Element.of("foo"));
        Assertions.assertTrue(listElement.get(2).isDict());
    }

    @Test
    void whenInvalidDictElementGet_thenThrowOrEmpty() {
        Assertions.assertThrows(ElementKeyNotPresentException.class, () -> dictElement.get("invalid_key"));
        Assertions.assertTrue(dictElement.find("invalid_key").isEmpty());
    }

    @Test
    void whenInvalidListElementGet_thenThrowOrEmpty() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> listElement.get(3));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> listElement.get(-1));
        Assertions.assertTrue(listElement.find(3).isEmpty());
        Assertions.assertTrue(listElement.find(-1).isEmpty());
    }

    @Test
    void whenIterating_thenVisitChildren() {
        Assertions.assertEquals(3, callCount(dictElement::each));
        Assertions.assertEquals(3, callCount(listElement::each));
    }

    @Test
    void whenCrawling_thenVisitDescendants() {
        Assertions.assertEquals(5, callCount(dictElement::crawl));
        Assertions.assertEquals(5, callCount(listElement::crawl));
    }

    private static int callCount(Consumer<Consumer<Element>> caller) {
        final AtomicInteger visited = new AtomicInteger();
        caller.accept(element -> visited.getAndIncrement());
        return visited.get();
    }
}
