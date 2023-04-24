package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.exception.ElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DataElementTest {
    private static DataElement testElement;

    @BeforeAll
    static void beforeAll() {
        testElement = new DataPrimitive();
    }

    @Test
    void whenIncorrectAs_thenThrow() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> testElement.asMap());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> testElement.asList());
        Assertions.assertDoesNotThrow(() -> testElement.asPrimitive());
    }

    @Test
    void whenIncorrectAsOrNull_thenNull() {
        Assertions.assertNull(testElement.asOrNull(DataMap.class));
        Assertions.assertNull(testElement.asOrNull(DataList.class));
        Assertions.assertNotNull(testElement.asOrNull(DataPrimitive.class));
    }

    @Test
    void testIsOf() {
        Assertions.assertTrue(testElement.isOf(DataPrimitive.class));
        Assertions.assertFalse(testElement.isOf(DataMap.class));
        Assertions.assertTrue(testElement.isPrimitive());
        Assertions.assertFalse(testElement.isMap());
    }

    @Test
    void testRequireOf() {
        Assertions.assertThrows(ElementException.class, () -> testElement.requireOf(DataList.class));
        Assertions.assertDoesNotThrow(() -> testElement.requireOf(DataPrimitive.class));
    }
}
