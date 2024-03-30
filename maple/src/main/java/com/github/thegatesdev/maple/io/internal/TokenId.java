package com.github.thegatesdev.maple.io.internal;

public interface TokenId {
    byte ID_NOT_AVAILABLE = -1;
    byte ID_NOT_PRESENT = 0;
    byte ID_OBJECT_START = 1;
    byte ID_OBJECT_END = 2;
    byte ID_ARRAY_START = 3;
    byte ID_ARRAY_END = 4;
    byte ID_FIELD_NAME = 5;
    byte ID_VALUE_STRING = 6;
    byte ID_VALUE_INTEGER = 7;
    byte ID_VALUE_DECIMAL = 8;
    byte ID_VALUE_TRUE = 9;
    byte ID_VALUE_FALSE = 10;
    byte ID_VALUE_NULL = 11;
}
