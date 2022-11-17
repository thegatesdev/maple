package com.thegates.maple.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

class DataMapTest {

    private static DataMap testMap;

    @BeforeEach
    @Test
    void put() {
        testMap = new DataMap();
        testMap.put("map_entry", new DataMap().put("nested_entry_1", new DataMap().put("nested_entry_2", new DataPrimitive("test"))));
        testMap.put("list_entry", new DataList().add(new DataPrimitive(new Object())));
        testMap.put("null_entry", new DataNull());
        testMap.put("primitive_entry", new DataPrimitive("test_2"));
    }


    @Test
    void string() {
        assert testMap.toString().equals("dataMap{'map_entry': dataMap{'nested_entry_1': dataMap{'nested_entry_2': dataPrimitive<String>}}, 'list_entry': dataList[dataPrimitive<Object>], 'null_entry': dataNull, 'primitive_entry': dataPrimitive<String>}");
    }

    @Test
    void requireKeys_size() {
        assert testMap.hasKeys("map_entry", "list_entry", "null_entry", "primitive_entry");
        assert testMap.size() == 4;
        assert testMap.get("map_entry").requireOf(DataMap.class).size() == 1;
    }

    @Test
    void get_is() {
        assert testMap.get("null_entry").isDataNull();
        assert testMap.get("map_entry").isDataMap();
        assert testMap.get("list_entry").isDataList();
        assert testMap.get("primitive_entry").isDataPrimitive();
    }

    @Test
    void ifPresent() {
        final AtomicReference<String> test = new AtomicReference<>();
        testMap.ifPresent("primitive_entry", String.class, test::set);
        assert test.get().equals("test_2");
    }

    @Test
    void navigate() {
        assert testMap.navigate("map_entry", "nested_entry_1", "nested_entry_2").requireOf(DataPrimitive.class).requireValue(String.class).equals("test");
    }

    @Test
    void parent() {
        assert testMap.get("map_entry").parent() == testMap;
    }

    @Test
    void name() {
        assert testMap.get("map_entry").name().equals("map_entry");
    }

    @Test
    void read_hasKeys_requireOf() {
        final DataMap read = DataMap.read(Map.of("1", 1, "2", new DataList()));
        assert read.hasKeys("1", "2");
        read.requireOf("1", DataPrimitive.class);
        read.requireOf("2", DataList.class);
    }

    @AfterEach
    void tearDown() {
        testMap = null;
    }
}