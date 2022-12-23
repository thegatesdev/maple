package io.github.thegatesdev.maple.data;

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
        //noinspection AssertWithSideEffects
        assert new DataMap("named").put("test_element", new DataPrimitive("test_value")).put("test_map", new DataMap().put("map_element", new DataPrimitive("test"))).hashCode() == testMap.hashCode();
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
        final String[] elements = testMap.getMap("test_map").get("map_element").path();
        assert elements[0].equals("named");
        assert elements[1].equals("test_map");
        assert elements[2].equals("map_element");

    }

    @BeforeEach
    void setUp() {
        rootedPrimitive = new DataPrimitive("test_value");// Setting value not name
        testMap = new DataMap("named").put("test_element", rootedPrimitive).put("test_map", new DataMap().put("map_element", new DataPrimitive("test")));
    }

    @AfterEach
    void tearDown() {
        testMap = null;
    }
}
