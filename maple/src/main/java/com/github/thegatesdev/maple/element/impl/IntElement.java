package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class IntElement implements Element {

    @Override
    public ElementType type() {
        return ElementType.NUMBER;
    }
}
