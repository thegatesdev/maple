package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.Element;

public interface Layout<T> {

    T parse(Element element);

    Element write(T value);
}
