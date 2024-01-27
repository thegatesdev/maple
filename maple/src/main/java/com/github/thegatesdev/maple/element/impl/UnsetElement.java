package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public enum UnsetElement implements Element {
    INSTANCE;

    public static UnsetElement getInstance() {
        return INSTANCE;
    }


    @Override
    public boolean isUnset() {
        return true;
    }

    @Override
    public ElementType type() {
        return ElementType.UNSET;
    }
}
