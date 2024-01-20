package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class BoolElement implements Element {

    @Override
    public ElementType type() {
        return ElementType.BOOLEAN;
    }
}
