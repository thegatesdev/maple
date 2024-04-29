package com.github.thegatesdev.maple.io.format.json.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;

public final class JsonDeserializer implements Deserializer {

    private final Reader reader;

    public JsonDeserializer(Reader reader) {
        this.reader = reader;
    }


    @Override
    public void next() {

    }

    @Override
    public void skip() {

    }

    @Override
    public Token current() {
        return null;
    }

    @Override
    public byte currentID() {
        return 0;
    }


    @Override
    public String readName() {
        return null;
    }

    @Override
    public String readValue() {
        return null;
    }

    @Override
    public String readString() {
        return null;
    }

    @Override
    public boolean readBool() {
        return false;
    }

    @Override
    public void readNull() {

    }
}
