package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.exception.KeyNotPresentException;
import io.github.thegatesdev.maple.exception.TypeMismatchException;

import java.util.Objects;
import java.util.function.Consumer;

public sealed interface DataDictionary<Key> permits DataMap, DataList {

    // Simple operations

    DataElement getOrNull(Key key);

    default DataElement getOrThrow(Key key) throws KeyNotPresentException {
        var el = getOrNull(key);
        if (el == null) throw new KeyNotPresentException(Objects.toString(key));
        return el;
    }

    void set(Key key, DataElement element);

    // Typed getters

    default Boolean getBool(Key key) throws KeyNotPresentException, TypeMismatchException {
        return getValue(key).getValueOrThrow(Boolean.class);
    }

    default Boolean getBool(Key key, Boolean def) throws KeyNotPresentException {
        return getValue(key).getValueOr(Boolean.class, def);
    }

    default Integer getInt(Key key) throws KeyNotPresentException, TypeMismatchException {
        return getValue(key).getValueOrThrow(Integer.class);
    }

    default Integer getInt(Key key, Integer def) throws KeyNotPresentException {
        return getValue(key).getValueOr(Integer.class, def);
    }

    default Long getLong(Key key) throws KeyNotPresentException, TypeMismatchException {
        return getValue(key).getValueOrThrow(Long.class);
    }

    default Long getLong(Key key, Long def) throws KeyNotPresentException {
        return getValue(key).getValueOr(Long.class, def);
    }

    default Number getNum(Key key) throws KeyNotPresentException, TypeMismatchException {
        return getValue(key).getValueOrThrow(Number.class);
    }

    default Number getNum(Key key, Number def) throws KeyNotPresentException {
        return getValue(key).getValueOr(Number.class, def);
    }

    default String getString(Key key) throws KeyNotPresentException, TypeMismatchException {
        return getValue(key).getValueOrThrow(String.class);
    }

    default String getString(Key key, String def) throws KeyNotPresentException {
        return getValue(key).getValueOr(String.class, def);
    }

    // Element getters

    default DataMap getMap(Key key) {
        return getOrThrow(key).asMap();
    }

    default DataList getList(Key key) {
        return getOrThrow(key).asList();
    }

    default DataValue<?> getValue(Key key) {
        return getOrThrow(key).asValue();
    }

    // Iteration

    void each(Consumer<DataElement> elementConsumer);

    default void eachMap(Consumer<DataMap> mapConsumer) {
        each(element -> element.ifMap(mapConsumer));
    }

    default void eachList(Consumer<DataList> mapConsumer) {
        each(element -> element.ifList(mapConsumer));
    }

    default void eachValue(Consumer<DataValue<?>> mapConsumer) {
        each(element -> element.ifValue(mapConsumer));
    }

    // Information

    int size();
}
