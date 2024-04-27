package com.github.thegatesdev.maple.io.internal;

public interface TokenID {
    byte NotPresent = 0;

    byte ObjectOpen = 1;
    byte ObjectClose = 2;
    byte ArrayOpen = 3;
    byte ArrayClose = 4;

    byte FieldName = 5;

    byte ValueString = 6;
    byte ValueNumber = 7;
    byte ValueTrue = 8;
    byte ValueFalse = 9;
    byte ValueNull = 10;
}
