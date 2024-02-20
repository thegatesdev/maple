package com.github.thegatesdev.maple.exception;

/**
 * This exception is raised when a key is used to access a value, but the key is not present.
 */
public final class ElementKeyNotPresentException extends ElementException {

    private static final String MESSAGE = "This key is not present; %s";
    private final String key;

    /**
     * Create the exception with the given key.
     *
     * @param key the accessed key
     */
    public ElementKeyNotPresentException(String key) {
        super(MESSAGE.formatted(key));
        this.key = key;
    }

    /**
     * Get the key that was accessed for this exception to be raised.
     *
     * @return the accessed key
     */
    public String key() {
        return key;
    }
}
