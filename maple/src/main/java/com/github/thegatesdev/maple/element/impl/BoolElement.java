package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public enum BoolElement implements Element {
    TRUE, FALSE;

    public static BoolElement of(boolean value) {
        return value ? TRUE : FALSE;
    }


    @Override
    public boolean isBool() {
        return true;
    }

    @Override
    public boolean getBool() {
        // I wonder what difference this makes to storing a boolean variable and returning that...
        // This would technically only require an address check vs actually looking up the stored boolean data.
        // Just a funny thought, the difference won't ever be noticeable.
        return this == TRUE;
    }

    @Override
    public ElementType type() {
        return ElementType.BOOLEAN;
    }
}
