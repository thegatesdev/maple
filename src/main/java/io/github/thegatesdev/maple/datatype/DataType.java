package io.github.thegatesdev.maple.datatype;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.element.DataMap;
import io.github.thegatesdev.maple.element.DataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class DataType<El extends DataElement> {

    // Creation

    private static final Map<Class<?>, DataType<?>> CACHE_PRIMITIVE_TYPES = new HashMap<>();


    public static <El extends DataElement> DataType<El> create(String typeId, Function<DataElement, El> readFunction) {
        return new DataType<>(typeId, readFunction);
    }

    public static <El extends DataElement> DataType<El> requireList(String typeId, Function<DataList, El> readFunction) {
        return new DataType<>(typeId, element -> readFunction.apply(element.requireList()));
    }

    public static <El extends DataElement> DataType<El> requireMap(String typeId, Function<DataMap, El> readFunction) {
        return new DataType<>(typeId, element -> readFunction.apply(element.requireMap()));
    }

    public static <El extends DataElement> DataType<El> requireValue(String typeId, Function<DataValue<?>, El> readFunction) {
        return new DataType<>(typeId, element -> readFunction.apply(element.requireValue()));
    }


    private static DataType<DataList> createList(DataType<?> original) {
        return requireList(original.typeId + "_list", list -> {
            var out = new ArrayList<DataElement>(list.size());
            list.each(element -> out.add(original.readFrom(element)));
            return new DataList(out);
        });
    }

    // Class

    private final String typeId;
    private final Function<DataElement, El> readFunction;
    private DataType<DataList> cachedListType;

    private DataType(String id, Function<DataElement, El> function) {
        typeId = id;
        readFunction = function;
    }

    public El readFrom(DataElement other) {
        return readFunction.apply(other);
    }

    public DataType<DataList> getListType() {
        if (cachedListType == null)
            cachedListType = createList(this);
        return cachedListType;
    }

    public String getTypeId() {
        return typeId;
    }
}
