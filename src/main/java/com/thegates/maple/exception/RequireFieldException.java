package com.thegates.maple.exception;

import com.thegates.maple.data.DataElement;

public class RequireFieldException extends ReadException {

    public RequireFieldException(DataElement data, String field) {
        super(data, "missing required field '%s'".formatted(field));
    }
}
