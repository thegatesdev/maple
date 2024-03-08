package com.github.thegatesdev.maple.element.impl;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ElementType;

/**
 * An element representing an unset or 'null' value.
 *
 * @author Timar Karels
 * @see ElementType#UNSET
 */
public enum UnsetElement implements Element {
    INSTANCE; // I started to really love enums for its unintended use-cases while making this.


    @Override
    public boolean isUnset() {
        return true;
    }

    @Override
    public ElementType type() {
        return ElementType.UNSET;
    }
}
