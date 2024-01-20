package com.github.thegatesdev.maple.element;

import com.github.thegatesdev.maple.element.impl.*;

public sealed interface Element permits DictElement, ListElement, BoolElement, DoubleElement, FloatElement, IntElement, LongElement, StringElement, UnsetElement {

}
