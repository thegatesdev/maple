package com.github.thegatesdev.maple;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;
import com.github.thegatesdev.maple.exception.LayoutParseException;
import com.github.thegatesdev.maple.layout.Layout;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class ElementLayoutTest {

    private static final DictElement dictElement = DictElement.builder(3)
            .put("int", Element.of(30))
            .put("string", Element.of("foo"))
            .put("nested", DictElement.builder(2)
                    .put("unset", Element.unset())
                    .put("bool", Element.of(true))
                    .build())
            .build();

    private static final Layout<DictElement> layout = Layout.dictionary()
            .optional("int", Element.of(31), ElementType.NUMBER)
            .required("string", ElementType.STRING)
            .required("nested", Layout.dictionary()
                    .required("unset")
                    .required("bool", ElementType.BOOLEAN)
                    .build())
            .build();

    @Test
    void whenCorrectLayout_thenPass() throws LayoutParseException {
        Assertions.assertDoesNotThrow(() -> layout.parse(dictElement));
        Assertions.assertTrue(layout.parse(dictElement).isDict());
    }

    @Test
    void whenOptionalNotPresent_thenReplaceDefault() throws LayoutParseException {
        DictElement optionalRemoved = dictElement.toBuilder().remove("int").build();
        DictElement parsed = layout.parse(optionalRemoved);
        Assertions.assertEquals(parsed.get("int"), Element.of(31));
    }

    @Test
    void whenRequiredNotPresent_thenThrow() {
        DictElement requiredRemoved = dictElement.toBuilder().remove("string").build();
        Assertions.assertThrows(LayoutParseException.class, () -> layout.parse(requiredRemoved));
    }

    @Test
    void whenInvalidType_thenThrow() {
        DictElement invalid = dictElement.toBuilder().put("string", Element.of(30)).build();
        Assertions.assertThrows(LayoutParseException.class, () -> layout.parse(invalid));
    }
}
