package com.thegates.maple.exception;

import com.thegates.maple.data.DataElement;

public class ReadException extends RuntimeException {

    public ReadException(DataElement data, String message) {
        super(String.format("Error while reading data at %s, %s.", data.getPath(), message));
    }
}
