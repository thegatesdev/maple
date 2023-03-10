package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.exception.ElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DataMapGetTest {

    private static DataMap testMap;

    @BeforeAll
    static void beforeAll() {
        testMap = new DataMap();
        testMap.put("null_value", new DataNull());
        testMap.put("int_value", new DataPrimitive(21));
        testMap.put("string_value", new DataPrimitive("foo"));
        testMap.put("map_value", new DataMap().put("nested_primitive", new DataPrimitive("bar")));
    }

    @Test
    void assertMapNotEmpty() {
        Assertions.assertFalse(testMap.isEmpty());
    }

    @Test
    void whenMapGetElement_thenReturnElement() {
        Assertions.assertEquals(new DataPrimitive("int_value",21), testMap.get("int_value"));
    }

    @Test
    void whenMapGetElementNotPresent_thenNullElement() {
        Assertions.assertTrue(testMap.get("non_present_element").isNull());
    }

    @Test
    void whenMapGetValueNull_thenThrow() {
        Assertions.assertThrows(ElementException.class, () -> testMap.getString("null_value"));
    }

    @Test
    void whenMapGetOrNullElementNotPresent_thenReturnNull() {
        Assertions.assertNull(testMap.getOrNull("non_present_element"));
    }

    @Test
    void whenMapGetInt_thenReturnInt() {
        Assertions.assertEquals(21, testMap.getInt("int_value"));
    }

    @Test
    void whenMapGetDefNotPresent_thenReturnDefault() {
        Assertions.assertEquals("bar", testMap.getString("non_present_string_value", "bar"));
    }

    @Test
    void assertHasKeys() {
        Assertions.assertTrue(testMap.hasKeys("null_value", "int_value", "string_value"));
    }

    @Test
    void whenMapGetUnsafe_thenReturnValue() {
        Assertions.assertEquals("foo", testMap.<String>getUnsafe("string_value"));
    }

    @Test
    void whenMapNavigate_thenReturnElement() {
        Assertions.assertEquals(new DataPrimitive("nested_primitive","bar"), testMap.navigate("map_value", "nested_primitive"));
    }

    @Test
    void whenMapClone_thenMapEquals() {
        Assertions.assertEquals(testMap, testMap.clone());
    }
}
