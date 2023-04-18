package io.github.thegatesdev.maple.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IndexedDataMapTest {

    private static IndexedDataMap testMap;

    @BeforeAll
    static void beforeAll() {
        testMap = new IndexedDataMap();
        testMap.put("first_el", new DataPrimitive(1));
        testMap.put("second_el", new DataPrimitive(2));
        testMap.put("third_el", new DataPrimitive(3));
    }

    @Test
    void whenGetCorrectIndex_thenReturnValue() {
        Assertions.assertEquals(1, testMap.get(0).value());
        Assertions.assertEquals(2, testMap.get(1).value());
        Assertions.assertEquals(3, testMap.get(2).value());
    }

    @Test
    void whenGetOutBounds_thenReturnNullValue() {
        Assertions.assertTrue(testMap.get(3).isNull());
        Assertions.assertNull(testMap.getOrNull(3));
    }
}
