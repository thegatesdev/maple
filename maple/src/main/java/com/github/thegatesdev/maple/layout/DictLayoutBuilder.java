package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.DictElement;
import com.github.thegatesdev.maple.element.Element;

public final class DictLayoutBuilder implements LayoutBuilder<DictElement> {

    @Override
    public Layout<DictElement> build() {
        return null;
    }

    public DictLayoutBuilder define(String key, Layout<? extends Element> layout) {

        return this;
    }

    public DictLayoutBuilder optional(String key, Layout<? extends Element> layout) {

        return this;
    }
}
