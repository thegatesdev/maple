package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class BoolElement implements Element {

    private final boolean value;

    private BoolElement(boolean value) {
        this.value = value;
    }


    @Override
    public boolean isBool() {
        return true;
    }

    @Override
    public boolean getBool() {
        return value;
    }

    @Override
    public ElementType type() {
        return ElementType.BOOLEAN;
    }
}
