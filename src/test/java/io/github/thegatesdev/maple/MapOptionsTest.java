package io.github.thegatesdev.maple;

import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.element.DataMap;
import io.github.thegatesdev.maple.element.DataValue;
import io.github.thegatesdev.maple.exception.KeyNotPresentException;
import io.github.thegatesdev.maple.read.DataType;
import io.github.thegatesdev.maple.read.MapOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MapOptionsTest {

    private static MapOptions<DataMap> testOptions;

    @BeforeAll
    static void setupTestOptions() {
        testOptions = MapOptions.builder()
                .add("option_one", DataType.integer())
                .add("option_two", DataType.bool(), false)
                .add("option_three", DataType.string().listType())
                .add("option_four", DataType.enumeration(ElementType.class), ElementType.MAP)
                .build();
    }

    @Test
    void whenCorrectOptions_thenValidate() {
        Assertions.assertDoesNotThrow(() -> testOptions.apply(DataMap.builder()
                .add("option_one", DataValue.of(1))
                .add("option_two", DataValue.of(true))
                .add("option_three", DataList.builder().add(DataValue.of("string_value")).build())
                .build()));
    }

    @Test
    void whenOptionNotPresent_thenThrow() {
        Assertions.assertThrows(KeyNotPresentException.class,
                () -> testOptions.apply(DataMap.builder()
                        .add("option_two", DataValue.of(true))
                        .add("option_three", DataList.builder().add(DataValue.of("string_value")).build())
                        .build()));
    }

    @Test
    void whenDefaultedOptionNotPresent_thenDefault() {
        DataMap result = testOptions.apply(DataMap.builder()
                .add("option_one", DataValue.of(1))
                .add("option_three", DataList.builder().add(DataValue.of("string_value")).build())
                .build());
        Assertions.assertEquals(result.getBool("option_two"), false);
        Assertions.assertEquals(result.get("option_four", ElementType.class), ElementType.MAP);
    }
}
