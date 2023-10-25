package io.github.thegatesdev.maple.exception;

public class KeyNotPresentException extends RuntimeException {

    private static final String MESSAGE = "Key '%s' is not present";
    private final String key;

    public KeyNotPresentException(String key) {
        super(MESSAGE.formatted(key));
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
