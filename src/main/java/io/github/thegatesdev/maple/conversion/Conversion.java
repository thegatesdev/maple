package io.github.thegatesdev.maple.conversion;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.element.DataMap;

import java.util.List;
import java.util.Map;

public interface Conversion {

    DataMap convertMap(Map<?, ?> someMap);

    DataList convertList(List<?> someList);
}
