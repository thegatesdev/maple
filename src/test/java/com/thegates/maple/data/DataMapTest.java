package com.thegates.maple.data;

import org.junit.jupiter.api.Test;

class DataMapTest {

    @Test
    void navigate() {
        DataMap testMap = new DataMap();
        testMap.put("hello", new DataMap().put("world", new DataPrimitive("Hello world!")));
        DataElement result = testMap.navigate("hello", "world");
        assert result.isDataPrimitive() && result.getAsDataPrimitive().getValue().equals("Hello world!");
    }
}