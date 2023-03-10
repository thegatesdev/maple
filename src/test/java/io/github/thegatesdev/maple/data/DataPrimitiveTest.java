package io.github.thegatesdev.maple.data;

import io.github.thegatesdev.maple.exception.ElementException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataPrimitiveTest {
    @Test
    void whenRequireCorrectValue_thenReturnValue() {
        Assertions.assertEquals("foo", new DataPrimitive("foo").requireValue(String.class));
    }

    @Test
    void whenRequireIncorrectValue_thenThrow() {
        Assertions.assertThrows(ElementException.class, () -> new DataPrimitive("foo").requireValue(Number.class));
    }

    @Test
    void assertValueOf() {
        DataPrimitive foo = new DataPrimitive("foo");
        Assertions.assertFalse(foo.valueOf(Number.class));
        Assertions.assertTrue(foo.valueOf(String.class));
    }

    @Test
    void whenValueOrNullIncorrectValue_thenReturnNull() {
        Assertions.assertNull(new DataPrimitive("foo").valueOrNull(Number.class));
    }

    @Test
    void whenValueOrNullCorrectValue_thenReturnValue() {
        Assertions.assertEquals("foo", new DataPrimitive("foo").valueOrNull(String.class));
    }

    @Test
    void assertCloneEquals() {
        DataPrimitive foo = new DataPrimitive("foo");
        Assertions.assertEquals(foo, foo.clone());
    }

    @Test
    void assertValueSet() {
        DataPrimitive foo = new DataPrimitive("foo");
        Assertions.assertEquals("foo", foo.value);
        foo.value("bar");
        Assertions.assertEquals("bar", foo.value);
    }
}
