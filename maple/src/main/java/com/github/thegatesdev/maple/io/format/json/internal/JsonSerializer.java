package com.github.thegatesdev.maple.io.format.json.internal;

import com.github.thegatesdev.maple.io.*;
import com.github.thegatesdev.maple.io.format.json.*;

import java.io.*;
import java.math.*;

public final class JsonSerializer implements Serializer {

    private final Writer writer;
    private final JsonOutputSettings settings;

    public JsonSerializer(Writer writer, JsonOutputSettings settings) {
        this.writer = writer;
        this.settings = settings;
    }


    private void openStructure(int openCharacter) {
        writeOrThrow(openCharacter);
    }

    private void closeStructure(int closeCharacter) {
        writeOrThrow(closeCharacter);
    }

    private void writePlainValue(String value) {
        writeOrThrow(value);
    }

    private void writeOrThrow(int value) {
        try {
            writer.write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeOrThrow(String value) {
        try {
            writer.write(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void openObject() {
        openStructure('{');
    }

    @Override
    public Serializer name(String name) {
        writeOrThrow('"' + name + '"' + ':');
        return this;
    }

    @Override
    public void closeObject() {
        closeStructure('}');
    }

    @Override
    public void openArray() {
        openStructure('[');
    }

    @Override
    public void closeArray() {
        closeStructure(']');
    }

    @Override
    public void value(String value) {
        writePlainValue('"' + value + '"');
    }

    @Override
    public void value(boolean value) {
        writePlainValue(Boolean.toString(value));
    }

    @Override
    public void value(int value) {
        writePlainValue(Integer.toString(value));
    }

    @Override
    public void value(long value) {
        writePlainValue(Long.toString(value));
    }

    @Override
    public void value(float value) {
        writePlainValue(Float.toString(value));
    }

    @Override
    public void value(double value) {
        writePlainValue(Double.toString(value));
    }

    @Override
    public void value(BigInteger value) {
        writePlainValue(value.toString());
    }

    @Override
    public void value(BigDecimal value) {
        writePlainValue(value.toString());
    }
}
