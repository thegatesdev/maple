package com.github.thegatesdev.maple.io.format.json.internal;

import com.github.thegatesdev.maple.io.*;

import java.math.*;

public final class JsonSerializer implements Serializer {

    private final Output output;
    private final ScopeStack scope = new ScopeStack();

    public JsonSerializer(Output output) {
        this.output = output;
    }


    private void verifyValueWrite() {
        char write;
        switch (scope.checkWriteValue()) {
            case ScopeStack.STATUS_OK:
            default:
                return;
            case ScopeStack.STATUS_NEEDS_COMMA:
                write = ',';
                break;
            case ScopeStack.STATUS_NEEDS_COLON:
                write = ':';
                break;
            case ScopeStack.STATUS_EXPECT_NAME:
                throw new IllegalStateException();
        }
        output.write(write);
    }


    @Override
    public void openObject() {
        verifyValueWrite();
        scope.pushObject();
        output.write('{');
    }

    @Override
    public void closeObject() {
        if (!scope.popObject()) throw new IllegalStateException(); // TODO figure out errors
        output.write('}');
    }

    @Override
    public void openArray() {
        verifyValueWrite();
        scope.pushArray();
        output.write('[');
    }

    @Override
    public void closeArray() {
        if (!scope.popArray()) throw new IllegalStateException(); // TODO figure out errors
        output.write(']');
    }


    @Override
    public void name(String name) {
        byte status = scope.checkWriteName();
        switch (status) {
            case ScopeStack.STATUS_EXPECT_VALUE:
                throw new IllegalStateException();
            case ScopeStack.STATUS_NEEDS_COMMA:
                output.write(',');
                break;
        }
        output.write('"' + name + '"');
    }

    @Override
    public void value(String value) {
        verifyValueWrite();
        output.write('"' + value + '"');
    }

    @Override
    public void value(boolean value) {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(int value) {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(long value) {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(float value) {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(double value) {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(BigInteger value) {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(BigDecimal value) {
        verifyValueWrite();
        output.writeValue(value);
    }
}
