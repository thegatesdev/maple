package com.thegates.maple.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

class DataMapTest {

    private DataMap testMap;
    private DataMap mapEntry;
    private DataList listEntry;

    @Test
    void get_is() {
        assert testMap.get("null_entry").isNull();
        assert testMap.get("map_entry").isMap();
        assert testMap.get("list_entry").isList();
        assert testMap.get("primitive_entry").isPrimitive();
    }

    @Test
    void ifPresent() {
        final AtomicReference<String> ifPresent = new AtomicReference<>();
        testMap.ifPresent("primitive_entry", el -> ifPresent.set(el.asPrimitive().stringValue()));
        assert ifPresent.get().equals("test_2");

        final AtomicReference<String> ifPrimitive = new AtomicReference<>();
        testMap.ifPrimitive("primitive_entry", primitive -> ifPrimitive.set(primitive.stringValue()));
        assert ifPrimitive.get().equals("test_2");

        final AtomicReference<DataMap> ifMap = new AtomicReference<>();
        testMap.ifMap("map_entry", ifMap::set);
        assert ifMap.get() == mapEntry;

        final AtomicReference<DataList> ifList = new AtomicReference<>();
        testMap.ifList("list_entry", ifList::set);
        assert ifList.get() == listEntry;
    }

    @Test
    void name() {
        assert testMap.get("map_entry").name().equals("map_entry");
    }

    @Test
    void navigate() {
        assert testMap.navigate("map_entry", "nested_entry_1", "nested_entry_2").requireOf(DataPrimitive.class).requireValue(String.class).equals("test");
    }

    @Test
    void parent() {
        assert testMap.get("map_entry").parent() == testMap;
    }

    @BeforeEach
    @Test
    void put() {
        testMap = new DataMap();
        mapEntry = new DataMap().put("nested_entry_1", new DataMap().put("nested_entry_2", new DataPrimitive("test")));
        testMap.put("map_entry", mapEntry);
        listEntry = new DataList().add(new DataPrimitive(new Object()));
        testMap.put("list_entry", listEntry);
        testMap.put("null_entry", new DataNull());
        testMap.put("primitive_entry", new DataPrimitive("test_2"));
    }

    @Test
    void read_hasKeys_requireOf() {
        final DataMap read = DataMap.read(Map.of("1", 1, "2", new DataList()));
        assert read.hasKeys("1", "2");
        read.requireOf("1", DataPrimitive.class);
        read.requireOf("2", DataList.class);
    }

    @Test
    void requireKeys_size() {
        assert testMap.hasKeys("map_entry", "list_entry", "null_entry", "primitive_entry");
        assert testMap.size() == 4;
        assert testMap.get("map_entry").requireOf(DataMap.class).size() == 1;
    }

    @Test
    void string() {
        assert testMap.toString().equals("dataMap{'map_entry': dataMap{'nested_entry_1': dataMap{'nested_entry_2': dataPrimitive<String>}}, 'list_entry': dataList[dataPrimitive<Object>], 'null_entry': dataNull, 'primitive_entry': dataPrimitive<String>}");
    }

    @AfterEach
    void tearDown() {
        testMap = null;
    }
}