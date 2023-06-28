package io.github.thegatesdev.maple.data;

public final class DataNull extends DataElement {

    @Override
    public Object view() {
        return null;
    }

    /**
     * @return {@code true}
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public DataElement copy() {
        return new DataNull();
    }

    @Override
    public String toString() {
        return "empty";
    }
}
