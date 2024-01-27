package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public enum BoolElement implements Element {
    TRUE, FALSE;

    public static BoolElement of(boolean value) {
        return value ? TRUE : FALSE;
    }

    public static BoolElement getTrue() {
        return TRUE;
    }

    public static BoolElement getFalse() {
        return FALSE;
    }


    @Override
    public boolean isBool() {
        return true;
    }

    @Override
    public boolean getBool() {
        return this == TRUE;
    }

    @Override
    public ElementType type() {
        return ElementType.BOOLEAN;
    }
}
