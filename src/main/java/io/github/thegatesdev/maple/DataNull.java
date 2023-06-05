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

    @Override
    public DataNull shallowCopy() {
        return new DataNull();
    }

    @Override
    public DataNull deepCopy() {
        return shallowCopy();
    }

    @Override
    public String toString() {
        return "empty";
    }
}
