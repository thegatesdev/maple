package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class UnsetElement implements Element {

    @Override
    public boolean isUnset() {
        return true;
    }

    @Override
    public ElementType type() {
        return ElementType.UNSET;
    }
}
