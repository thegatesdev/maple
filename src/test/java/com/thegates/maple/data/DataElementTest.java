package com.thegates.maple.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataElementTest {

    private DataMap testMap;
    private DataPrimitive rootedPrimitive;

    @Test
    void cloneEqualTest() {
        assert testMap.equals(testMap.clone().name("named"));
    }

    @Test
    void getAsOrNull() {
        assert testMap.navigate("test_element").asOrNull(DataMap.class) == null;
        assert testMap.navigate("test_element").asOrNull(DataPrimitive.class) != null;
    }

    @Test
    void hashCodeTest() {
        assert new DataMap("named").put("test_element", new DataPrimitive("test_value")).hashCode() == testMap.hashCode();
    }

    @Test
    void isOf() {
        assert rootedPrimitive.isOf(DataPrimitive.class);
    }

    @Test
    void parentsAmount() {
        assert rootedPrimitive.parents() == 1;
    }

    @Test
    void path() {
        final String[] elements = testMap.navigate("test_element").path();
        assert elements[0].equals("named");
        assert elements[1].equals("test_element");
    }

    @BeforeEach
    void setUp() {
        rootedPrimitive = new DataPrimitive("test_value");
        testMap = new DataMap("named").put("test_element", rootedPrimitive);
    }

    @AfterEach
    void tearDown() {
        testMap = null;
    }
}
