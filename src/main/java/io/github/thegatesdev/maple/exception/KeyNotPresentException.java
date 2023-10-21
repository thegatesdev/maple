package io.github.thegatesdev.maple.exception;

public class KeyNotPresentException extends IllegalArgumentException {

    private final String keyName;

    public KeyNotPresentException(String keyName) {
        super("Key '%s' is not present".formatted(keyName));
        this.keyName = keyName;
    }

    public String getKeyName() {
        return keyName;
    }
}
