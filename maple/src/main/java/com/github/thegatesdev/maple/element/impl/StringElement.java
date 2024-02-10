package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

import java.util.Objects;

public final class StringElement implements Element {

    private final String value;

    private StringElement(String value) {
        this.value = value;
    }


    public static StringElement of(String value) {
        Objects.requireNonNull(value, "value cannot be null");
        return new StringElement(value);
    }


    @Override
    public boolean isString() {
        return true;
    }

    @Override
    public String getString() {
        return value;
    }

    @Override
    public ElementType type() {
        return ElementType.STRING;
    }

    @Override
    public String toString() {
        return "string<" + value + ">";
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof StringElement stringElement)) return false;
        return value.equals(stringElement.value);
    }
}
