package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.read.types.EnumDataType;
import io.github.thegatesdev.maple.read.types.ListDataType;
import io.github.thegatesdev.maple.read.types.ValueDataType;

public interface DataType<R extends DataElement> {

    R read(DataElement input);

    String getId();

    default ListDataType listType() {
        return DataType.list(this);
    }


    static String typeToIdentifier(Class<?> type) {
        return type.getSimpleName().replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    // Creation

    static <Val> ValueDataType<Val> value(Class<Val> valueType) {
        return ValueDataType.getOrCreate(valueType);
    }

    static ValueDataType<String> string() {
        return value(String.class);
    }

    static ValueDataType<Integer> integer() {
        return value(Integer.class);
    }

    static ValueDataType<Number> number() {
        return value(Number.class);
    }

    static ValueDataType<Boolean> bool() {
        return value(Boolean.class);
    }

    static <E extends Enum<E>> EnumDataType<E> enumeration(Class<E> valueType) {
        return EnumDataType.getOrCreate(valueType);
    }

    static ListDataType list(DataType<?> original) {
        return ListDataType.getOrCreate(original);
    }
}
