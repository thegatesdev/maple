package com.github.thegatesdev.maple.io.internal;

import static com.github.thegatesdev.maple.io.internal.TokenId.*;

public enum Token {
    NOT_AVAILABLE(ID_NOT_AVAILABLE),
    NOT_PRESENT(ID_NOT_PRESENT),
    OBJECT_START(ID_OBJECT_START),
    OBJECT_END(ID_OBJECT_END),
    ARRAY_START(ID_ARRAY_START),
    ARRAY_END(ID_ARRAY_END),
    FIELD_NAME(ID_FIELD_NAME),
    VALUE_STRING(ID_VALUE_STRING),
    VALUE_INTEGER(ID_VALUE_INTEGER),
    VALUE_DECIMAL(ID_VALUE_DECIMAL),
    VALUE_TRUE(ID_VALUE_TRUE),
    VALUE_FALSE(ID_VALUE_FALSE),
    VALUE_NULL(ID_VALUE_NULL);

    public final int id;

    Token(int id) {
        this.id = id;
    }
}
