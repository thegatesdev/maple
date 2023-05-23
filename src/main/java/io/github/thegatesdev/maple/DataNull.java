package io.github.thegatesdev.maple;

/**
 * An element without any value.
 */
public class DataNull extends DataElement {

    DataNull() {
    }

    // -- ELEMENT

    @Override
    protected Object raw() {
        return null;
    }

    @Override
    public Object view() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
