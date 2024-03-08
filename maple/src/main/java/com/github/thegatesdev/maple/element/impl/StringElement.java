package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

import java.util.Objects;

public record StringElement(String value) implements Element {

    public StringElement {
        Objects.requireNonNull(value, "given string is null");
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
}
