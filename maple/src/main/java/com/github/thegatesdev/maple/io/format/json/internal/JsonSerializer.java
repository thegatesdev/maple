package com.github.thegatesdev.maple.io.format.json.internal;

import com.github.thegatesdev.maple.exception.*;
import com.github.thegatesdev.maple.io.*;

import java.math.*;

public final class JsonSerializer implements Serializer {

    private final Output output;
    private final ScopeStack scope = new ScopeStack();

    private final int[] escapes = new int[35];

    {
        escapes[0x22] = 1;
    }

    public JsonSerializer(Output output) {
        this.output = output;
    }


    private void verifyValueWrite() throws OutputException {
        char write;
        switch (scope.writeValueStatus()) {
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
                throw new IllegalStateException("expected name before value");
        }
        output.write(write);
    }


    @Override
    public void openObject() throws OutputException {
        verifyValueWrite();
        scope.pushObject();
        output.write('{');
    }

    @Override
    public void closeObject() throws OutputException {
        if (!scope.popObject()) throw new IllegalStateException("scope != object"); // TODO figure out errors
        output.write('}');
    }

    @Override
    public void openArray() throws OutputException {
        verifyValueWrite();
        scope.pushArray();
        output.write('[');
    }

    @Override
    public void closeArray() throws OutputException {
        if (!scope.popArray()) throw new IllegalStateException("scope != array"); // TODO figure out errors
        output.write(']');
    }


    @Override
    public void name(String name) throws OutputException {
        byte status = scope.writeNameStatus();
        switch (status) {
            case ScopeStack.STATUS_EXPECT_VALUE:
                throw new IllegalStateException("expected value");
            case ScopeStack.STATUS_NEEDS_COMMA:
                output.write(',');
                break;
        }
        output.write('"');
        output.writeEscaped(name, escapes);
        output.write('"');
    }

    @Override
    public void value(String value) throws OutputException {
        verifyValueWrite();
        output.write('"');
        output.writeEscaped(value, escapes);
        output.write('"');
    }

    @Override
    public void value(boolean value) throws OutputException {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(int value) throws OutputException {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(long value) throws OutputException {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(float value) throws OutputException {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(double value) throws OutputException {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(BigInteger value) throws OutputException {
        verifyValueWrite();
        output.writeValue(value);
    }

    @Override
    public void value(BigDecimal value) throws OutputException {
        verifyValueWrite();
        output.writeValue(value);
    }
}
