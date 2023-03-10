package io.github.thegatesdev.maple.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class DataListGetTest {

    private static DataList testList;

    @BeforeAll
    static void beforeAll() {
        testList = new DataList();
        testList.add(new DataNull());
        testList.add(new DataPrimitive("foo"));
        testList.add(new DataPrimitive(21));
    }

    @Test
    void whenGetOrNullInBound_thenReturnValue() {
        Assertions.assertNotNull(testList.getOrNull(1));
    }

    @Test
    void assertSize() {
        Assertions.assertEquals(3, testList.size());
    }

    @Test
    void assertPrimitiveList() {
        ArrayList<String> actualList = testList.primitiveList(String.class);
        ArrayList<Object> expectedList = new ArrayList<>();
        expectedList.add("foo");
        Assertions.assertEquals(expectedList, actualList);
    }

    @Test
    void assertCloneEquals() {
        Assertions.assertEquals(testList, testList.clone());
    }
}
