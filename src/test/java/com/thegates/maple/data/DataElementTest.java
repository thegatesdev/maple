package com.thegates.maple.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataElementTest {

    private DataMap testMap;

    @BeforeEach
    void setUp() {
        testMap = new DataMap("named").put("test_element", new DataPrimitive("test_value"));
    }

    @Test
    void path() {
        assert testMap.navigate("test_element").path().equals("named.test_element");
    }

    @Test
    void hashCodeTest() {
        assert new DataMap("named").put("test_element", new DataPrimitive("test_value")).hashCode() == testMap.hashCode();
    }

    @Test
    void isOf() {
        assert testMap.navigate("test_element").isOf(DataPrimitive.class);
    }

    @Test
    void getAsOrNull() {
        assert testMap.navigate("test_element").asOrNull(DataMap.class) == null;
        assert testMap.navigate("test_element").asOrNull(DataPrimitive.class) != null;
    }

    @Test
    void cloneEqualTest() {
        assert testMap.equals(testMap.clone().setData(null, "named"));
    }

    @AfterEach
    void tearDown() {
        testMap = null;
    }
}
