package com.github.thegatesdev.maple;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;
import com.github.thegatesdev.maple.exception.ElementKeyNotPresentException;
import com.github.thegatesdev.maple.exception.ElementTypeException;
import com.github.thegatesdev.maple.layout.Layout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class ElementLayoutTest {

    private static final DictElement dictElement = DictElement.builder(3)
            .put("int", Element.of(30))
            .put("string", Element.of("foo"))
            .put("nested", DictElement.builder(2)
                    .put("bool", Element.of(true))
                    .build())
            .build();

    private static final Layout<DictElement> layout = Layout.dictionary()
            .optional("int", ElementType.NUMBER, Element.of(31))
            .required("string", ElementType.STRING)
            .required("nested", Layout.dictionary()
                    .required("bool", ElementType.BOOLEAN)
                    .build())
            .build();

    @Test
    void whenCorrectLayout_thenPass() {
        Assertions.assertDoesNotThrow(() -> layout.apply(dictElement));
        Assertions.assertTrue(layout.apply(dictElement).orElse(dictElement).isDict());
    }

    @Test
    void whenOptionalNotPresent_thenReplaceDefault() {
        DictElement optionalRemoved = dictElement.toBuilder().remove("int").build();
        DictElement parsed = layout.apply(optionalRemoved).orElse(optionalRemoved);
        Assertions.assertEquals(parsed.get("int"), Element.of(31));
    }

    @Test
    void whenRequiredNotPresent_thenThrow() {
        DictElement requiredRemoved = dictElement.toBuilder().remove("string").build();
        Assertions.assertThrows(ElementKeyNotPresentException.class, () -> layout.apply(requiredRemoved));
    }

    @Test
    void whenInvalidType_thenThrow() {
        DictElement invalid = dictElement.toBuilder().put("string", Element.of(30)).build();
        Assertions.assertThrows(ElementTypeException.class, () -> layout.apply(invalid));
    }
}
