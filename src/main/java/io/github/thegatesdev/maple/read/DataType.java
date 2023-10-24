package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.element.DataElement;
import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.element.DataMap;
import io.github.thegatesdev.maple.element.DataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class DataType<El extends DataElement> {

    // Creation

    private static final Map<Class<?>, DataType<?>> CACHE_PRIMITIVE_TYPES = new HashMap<>();


    public static <El extends DataElement> DataType<El> of(String typeId, Function<DataElement, El> readFunction) {
        return new DataType<>(typeId, readFunction);
    }

    public static <El extends DataElement> DataType<El> ofList(String typeId, Function<DataList, El> readFunction) {
        return new DataType<>(typeId, element -> readFunction.apply(element.requireList()));
    }

    public static <El extends DataElement> DataType<El> ofMap(String typeId, Function<DataMap, El> readFunction) {
        return new DataType<>(typeId, element -> readFunction.apply(element.requireMap()));
    }

    public static <El extends DataElement> DataType<El> ofValue(String typeId, Function<DataValue<?>, El> readFunction) {
        return new DataType<>(typeId, element -> readFunction.apply(element.requireValue()));
    }

    @SuppressWarnings("unchecked")
    private static <P> DataType<DataValue<P>> ofPrimitive(Class<P> primitiveType){
        return (DataType<DataValue<P>>) CACHE_PRIMITIVE_TYPES.computeIfAbsent(primitiveType, DataType::createPrimitiveType);
    }


    public static DataType<DataValue<String>> string() {
        return ofPrimitive(String.class);
    }

    public static DataType<DataValue<Integer>> integer() {
        return ofPrimitive(Integer.class);
    }

    public static DataType<DataValue<Number>> number() {
        return ofPrimitive(Number.class);
    }

    public static DataType<DataValue<Boolean>> bool() {
        return ofPrimitive(Boolean.class);
    }

    @SuppressWarnings("unchecked")
    public static <F extends Enum<F>> DataType<DataValue<F>> enumeration(Class<F> enumClass) {
        return (DataType<DataValue<F>>) CACHE_PRIMITIVE_TYPES.computeIfAbsent(enumClass, aClass -> createEnumType(enumClass));
    }


    private static <P> DataType<DataValue<P>> createPrimitiveType(Class<P> primitiveType){
        return ofValue(createTypeId(primitiveType), value -> value.getAsHolding(primitiveType));
    }

    private static <F extends Enum<F>> DataType<DataValue<F>> createEnumType(Class<F> enumClass){
        return ofValue(createTypeId(enumClass), value -> value
            .getAsHolding(String.class)
            .transform(enumClass, s -> Enum.valueOf(enumClass, s.toUpperCase().replaceAll("\\s+", "_"))));
    }

    private static DataType<DataList> createListType(DataType<?> original) {
        return ofList(original.typeId + "_list", list -> {
            var out = new ArrayList<DataElement>(list.size());
            list.each(element -> out.add(original.readFrom(element)));
            return new DataList(out);
        });
    }


    // Util

    private static String createTypeId(Class<?> type){
        return type.getSimpleName().toLowerCase();
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
            cachedListType = createListType(this);
        return cachedListType;
    }

    public String getTypeId() {
        return typeId;
    }


    @Override
    public int hashCode() {
        return Objects.hash(readFunction, typeId);
    }
}
