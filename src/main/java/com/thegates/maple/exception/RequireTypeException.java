package com.thegates.maple.exception;

import com.thegates.maple.data.DataElement;

public class RequireTypeException extends ReadException {
    public RequireTypeException(DataElement data, Class<?> type) {
        this(data, type.getSimpleName());
    }

    public RequireTypeException(DataElement data, String typeName) {
        super(data, "should be of " + typeName);
    }
}
