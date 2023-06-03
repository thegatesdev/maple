package io.github.thegatesdev.maple;

import io.github.thegatesdev.maple.exception.ElementException;

/**
 * An element for holding a single value that may change.
 */
public abstract class DataValue extends DataElement {

    private final Class<?> valueType;

    /**
     * @param valueType The type of the value contained in this DataValue.
     */
    protected DataValue(Class<?> valueType) {
        this.valueType = valueType;
    }

    /**
     * @return The type of the value contained in this DataValue.
     */
    public Class<?> valueType() {
        return valueType;
    }

    // -- VALUE

    /**
     * @param expectedType The class the value should be an instance of.
     * @param <T>          The type to get the value as.
     * @return The cast value, or {@code null} if the value is not an instance of {@code expectedType}.
     */
    public <T> T valueOrNull(Class<T> expectedType) {
        return valueOf(expectedType) ? valueUnsafe() : null;
    }

    /**
     * @param clazz The class the value should be of.
     * @param <T>   The type the value should be of.
     * @return The cast value.
     * @throws ElementException If the value is not an instance of {@code clazz}.
     */
    public <T> T valueOrThrow(Class<T> clazz) throws ElementException {
        if (!valueOf(clazz)) throw ElementException.requireType(this, clazz);
        return valueUnsafe();
    }

    /**
     * @param expectedType The class the value should conform to.
     * @return True if the value of this primitive conforms to clazz
     */
    public boolean valueOf(Class<?> expectedType) {
        return expectedType.isAssignableFrom(valueType);
    }

    /**
     * Unsafely casts the value to {@code T}.
     *
     * @param <T> The type to cast the value to.
     * @return The cast value.
     */
    @SuppressWarnings("unchecked")
    public <T> T valueUnsafe() {
        return (T) raw();
    }


    // -- ELEMENT

    @Override
    public Object view() {
        return raw();
    }
}
