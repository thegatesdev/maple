package io.github.thegatesdev.maple;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.element.DataMap;
import io.github.thegatesdev.maple.element.DataValue;
import io.github.thegatesdev.maple.exception.ElementTypeException;
import io.github.thegatesdev.maple.exception.KeyNotPresentException;
import io.github.thegatesdev.maple.exception.ValueTypeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

class DataDictionaryTest {

    private static DataMap testMap;
    private static DataList testList;

    @BeforeAll
    static void setupTestMap() {
        testMap = DataMap.builder(3)
                .add("key_one", DataValue.of("value_one"))
                .add("key_two", DataList.EMPTY)
                .add("key_three", DataMap.builder().add("key_nested", DataValue.of("value_nested")).build())
                .build();
    }

    @BeforeAll
    static void setupTestList() {
        testList = DataList.builder(3)
                .add(DataValue.of("value_one"))
                .add(DataList.EMPTY)
                .add(DataMap.builder().add("key_nested", DataValue.of("value_nested")).build())
                .build();
    }


    @Test
    void whenElementPresent_thenElementGet() {
        Assertions.assertDoesNotThrow(() -> testMap.get("key_one"));
        Assertions.assertDoesNotThrow(() -> testMap.get("key_two"));
        Assertions.assertDoesNotThrow(() -> testList.get(0));
        Assertions.assertDoesNotThrow(() -> testList.get(1));
    }

    @Test
    void whenNotMapElement_thenThrowOrNull() {
        Assertions.assertThrows(KeyNotPresentException.class, () -> testMap.get("key_invalid"));
        Assertions.assertNull(testMap.find("key_invalid"));
        Assertions.assertThrows(KeyNotPresentException.class, () -> testList.get(4));
        Assertions.assertNull(testList.find(4));
    }

    @Test
    void whenInvalidElementType_thenThrow() {
        Assertions.assertThrows(ElementTypeException.class, () -> testMap.getMap("key_one"));
        Assertions.assertDoesNotThrow(() -> testMap.getList("key_two"));
        Assertions.assertThrows(ElementTypeException.class, () -> testList.getMap(0));
        Assertions.assertDoesNotThrow(() -> testList.getList(1));
    }

    @Test
    void whenInvalidValueType_thenThrowOrDefault() {
        Assertions.assertThrows(ValueTypeException.class, () -> testMap.getInt("key_one"));
        Assertions.assertEquals(testMap.getInt("key_one", 1), 1);
        Assertions.assertThrows(ValueTypeException.class, () -> testList.getInt(0));
        Assertions.assertEquals(testList.getInt(0, 1), 1);
    }

    @Test
    void whenTransforming_visitOnlyChildren() {
        Assertions.assertEquals(transformCount(testMap), 3);
        Assertions.assertEquals(transformCount(testList), 3);
    }

    @Test
    void whenCrawlingMap_visitAllDescendants() {
        Assertions.assertEquals(crawlCount(testMap), 4);
        Assertions.assertEquals(crawlCount(testList), 4);
    }


    private static int crawlCount(DataElement element) {
        final AtomicInteger visited = new AtomicInteger();
        element.crawl(val -> {
            visited.getAndIncrement();
            return val;
        });
        return visited.get();
    }

    private static int transformCount(DataElement element) {
        final AtomicInteger visited = new AtomicInteger();
        element.transform(val -> {
            visited.getAndIncrement();
            return val;
        });
        return visited.get();
    }
}
