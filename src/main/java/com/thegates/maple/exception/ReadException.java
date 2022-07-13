package com.thegates.maple.exception;

import com.thegates.maple.data.DataElement;

public class ReadException extends RuntimeException {

    public ReadException(String where, String message) {
        super(String.format("Error while data at %s, %s.", where, message));
    }

    public ReadException(DataElement data, String message) {
        this(data.getDescription(), message);
    }
}
