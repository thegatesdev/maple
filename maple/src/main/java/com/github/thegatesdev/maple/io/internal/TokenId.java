package com.github.thegatesdev.maple.io.internal;

public interface TokenId {
    byte ID_NOT_AVAILABLE = -1;
    byte ID_NOT_PRESENT = 0;

    // Structural
    byte ID_BEGIN_OBJECT = 1;
    byte ID_BEGIN_ARRAY = 2;
    byte ID_END_OBJECT = 3;
    byte ID_END_ARRAY = 4;
    byte ID_NAME_SEPARATOR = 5;
    byte ID_VALUE_SEPARATOR = 6;
    // Values
    byte ID_VALUE_TRUE = 7;
    byte ID_VALUE_FALSE = 8;
    byte ID_VALUE_NULL = 9;
    byte ID_VALUE_NUMBER = 10;
    byte ID_VALUE_STRING = 11;
}
