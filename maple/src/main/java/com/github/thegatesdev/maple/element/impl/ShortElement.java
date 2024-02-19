package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

public final class ShortElement implements Element {

    static final ShortElement ZERO = new ShortElement((short) 0);

    private final short value;

    private ShortElement(short value) {
        this.value = value;
    }


    public static Element of(short value) {
        if (value == 0) return ZERO;
        return new ShortElement(value);
    }


    @Override
    public boolean isNumber() {
        return true;
    }

    @Override
    public short getShort() {
        return value;
    }

    @Override
    public int getInt() {
        return value;
    }

    @Override
    public long getLong() {
        return value;
    }

    @Override
    public float getFloat() {
        return value;
    }

    @Override
    public double getDouble() {
        return value;
    }

    @Override
    public ElementType type() {
        return ElementType.NUMBER;
    }

    @Override
    public String toString() {
        return "number<" + value + "S>";
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ShortElement shortElement)) return false;
        return value == shortElement.value;
    }
}
