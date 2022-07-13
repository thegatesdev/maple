package com.thegates.maple.exception;

import com.thegates.maple.data.DataElement;

public class RequireFieldException extends ReadException {

    public RequireFieldException(String where, String field) {
        super(where, String.format("missing required field '%s'", field));
    }

    public RequireFieldException(DataElement data, String field) {
        this(data.getDescription(), field);
    }
}
