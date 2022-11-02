package com.thegates.maple.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataMapTest {

    private DataMap testMap;

    @BeforeEach
    void setUp() {
        testMap = new DataMap();
        testMap.put("map_entry", new DataMap().put("nested_entry_1", new DataMap().put("nested_entry_2", new DataPrimitive(new Object()))));
        testMap.put("list_entry", new DataList().add(new DataPrimitive(new Object())));
        testMap.put("null_entry", new DataNull());
        testMap.put("primitive_entry", new DataPrimitive(new Object()));
    }

    @Test
    void requireKeys() {
        assert testMap.hasKeys("map_entry", "list_entry", "null_entry", "primitive_entry");
    }

    @Test
    void get() {
        assert testMap.get("null_entry").isDataNull();
        assert testMap.get("map_entry").isDataMap();
    }

    @Test
    void navigate() {
        assert testMap.navigate("map_entry", "nested_entry_1", "nested_entry_2").isDataPrimitive();
    }

    @AfterEach
    void tearDown() {
        testMap = null;
    }
}