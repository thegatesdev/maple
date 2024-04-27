package com.github.thegatesdev.maple.io;

public enum Token {
    NotPresent,

    ObjectOpen,
    ObjectClose,
    ArrayOpen,
    ArrayClose,

    FieldName,

    ValueString,
    ValueNumber,
    ValueTrue,
    ValueFalse,
    ValueNull
}
