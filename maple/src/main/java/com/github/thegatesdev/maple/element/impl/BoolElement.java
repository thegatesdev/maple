package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

import java.util.Optional;

public final class BoolElement implements Element {

    private final boolean value;

    public BoolElement(boolean value) {
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
    public Optional<Boolean> grabBool() {
        return Optional.of(value);
    }

    @Override
    public ElementType type() {
        return ElementType.BOOLEAN;
    }
}
