package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.Element;

@FunctionalInterface
public interface Layout<T> {

    static DictLayout.Builder dictionary() {
        return new DictLayout.Builder();
    }

    T parse(Element value);
}
