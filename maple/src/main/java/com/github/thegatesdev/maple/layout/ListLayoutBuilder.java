package com.github.thegatesdev.maple.layout;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.element.ListElement;

public final class ListLayoutBuilder implements LayoutBuilder<ListElement> {

    @Override
    public Layout<ListElement> build() {
        return null;
    }

    public ListLayoutBuilder define(int index, Layout<? extends Element> layout) {

        return this;
    }

    public ListLayoutBuilder optional(int index, Layout<? extends Element> layout) {

        return this;
    }

    public ListLayoutBuilder countBetween(int min, int max) {

        return this;
    }
}
