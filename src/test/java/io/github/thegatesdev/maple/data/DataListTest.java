package io.github.thegatesdev.maple.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DataListTest {
    private DataList dataList;

    @BeforeEach
    void setUp() {
        dataList = new DataList();
        dataList.add(new DataPrimitive("string_test"));
        dataList.add(new DataMap());
        dataList.add(new DataList());
        assert dataList.size() == 3;
    }

    @Test
    void read_addAll() {
        final DataList read = DataList.read("one", "two", "three");
        assert read.size() == 3;
        dataList.cloneFrom(read);
        assert dataList.size() == 6;
    }

    @Test
    void primitiveList() {
        final List<String> strings = dataList.primitiveList(String.class);
        assert strings.size() == 1;
        assert strings.get(0).equals("string_test");
    }
}
