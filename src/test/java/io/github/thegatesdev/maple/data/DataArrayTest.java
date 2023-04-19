package io.github.thegatesdev.maple.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DataArrayTest {

    private static DataArray testArray;

    @BeforeAll
    static void beforeAll() {
        testArray = new DataArray(3);
        testArray.set(0,new DataNull());
        testArray.set(1,new DataPrimitive("foo"));
        testArray.set(2,new DataPrimitive(21));
    }

    @Test
    void whenGetOrNullInBound_thenReturnValue() {
        Assertions.assertNotNull(testArray.getOrNull(1));
    }

    @Test
    void whenGetIncorrect_thenReturnNull() {
        Assertions.assertTrue(testArray.get(4).isNull());
    }

    @Test
    void assertSize() {
        Assertions.assertEquals(3, testArray.size());
    }

    @Test
    void assertCloneEquals() {
        Assertions.assertEquals(testArray, testArray.clone());
    }
}
