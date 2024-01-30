package com.github.thegatesdev.maple.exception;

public class ElementKeyNotPresentException extends ElementException {

    private static final String MESSAGE = "This key is not present; %s";
    private final String key;

    public ElementKeyNotPresentException(String key) {
        super(MESSAGE.formatted(key));
        this.key = key;
    }

    public String key() {
        return key;
    }
}
