package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

import java.util.Optional;

public final class StringElement implements Element {

    private final String value;

    public StringElement(String value) {
        this.value = value;
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
    public String getString(String def) {
        return getString();
    }

    @Override
    public Optional<String> grabString() {
        return Optional.of(value);
    }

    @Override
    public ElementType type() {
        return ElementType.STRING;
    }
}
