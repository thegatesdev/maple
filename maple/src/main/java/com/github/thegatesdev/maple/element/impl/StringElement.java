package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class StringElement implements Element {

    private final String value;

    private StringElement(String value) {
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
    public ElementType type() {
        return ElementType.STRING;
    }
}
