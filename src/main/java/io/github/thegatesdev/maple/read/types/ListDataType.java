package io.github.thegatesdev.maple.read.types;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.read.DataType;

import java.util.HashMap;
import java.util.Map;

public class ListDataType implements DataType<DataList> {

    private static final Map<DataType<?>, ListDataType> CACHE = new HashMap<>();

    private final DataType<?> original;
    private final String id;

    private ListDataType(DataType<?> original) {
        this.original = original;
        this.id = original.getId() + "_list";
    }

    public static ListDataType getOrCreate(DataType<?> original) {
        return CACHE.computeIfAbsent(original, ListDataType::new);
    }


    @Override
    public DataList read(DataElement input) {
        var list = input.asList();
        var out = new DataList(list.size());
        list.each(element -> out.add(original.read(element)));
        return out;
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public int hashCode() {
        return 524287 * original.hashCode();
    }
}
