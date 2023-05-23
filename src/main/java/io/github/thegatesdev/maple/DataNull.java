package io.github.thegatesdev.maple;

public class DataNull extends DataElement {
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
    public DataElement clone() {
        return new DataNull();
    }


    @Override
    public boolean isNull() {
        return true;
    }
}
