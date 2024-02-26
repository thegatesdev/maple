package com.github.thegatesdev.maple;

import com.github.thegatesdev.maple.adapt.SnakeYamlAdapter;
import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ListElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

final class AdaptionTest {

    private static final Load yamlLoad = new Load(LoadSettings.builder().setLabel("Test configuration").build());
    private static final Object loaded = yamlLoad.loadFromInputStream(AdaptionTest.class.getClassLoader().getResourceAsStream("yaml-test.yml"));
    private static final Element parsed = new SnakeYamlAdapter().adapt(loaded);

    @Test
    void testStructure() {
        Assertions.assertTrue(parsed.isDict());
        DictElement dictElement = parsed.getDict();
        Assertions.assertEquals(dictElement.get("int"), Element.of(30));
        Assertions.assertEquals(dictElement.get("string"), Element.of("foo"));
        Assertions.assertTrue(dictElement.get("nested").isDict());
        DictElement nested = dictElement.get("nested").getDict();
        Assertions.assertTrue(nested.get("unset").isUnset());
        Assertions.assertEquals(nested.get("bool"), Element.of(true));
        Assertions.assertTrue(dictElement.get("list").isList());
        ListElement listElement = dictElement.get("list").getList();
        Assertions.assertEquals(listElement.count(), 2);
        Assertions.assertEquals(listElement.get(0), Element.of("value_one"));
    }
}
