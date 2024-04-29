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
        output.push(write);
    }


    @Override
    public void openObject() {
        verifyValueWrite();
        scope.pushObject();
        output.push('{');
    }

    @Override
    public void closeObject() {
        if (!scope.popObject()) throw new IllegalStateException(); // TODO figure out errors
        output.push('}');
    }

    @Override
    public void openArray() {
        verifyValueWrite();
        scope.pushArray();
        output.push('[');
    }

    @Override
    public void closeArray() {
        if (!scope.popArray()) throw new IllegalStateException(); // TODO figure out errors
        output.push(']');
    }


    @Override
    public void name(String name) {
        byte status = scope.checkWriteName();
        switch (status) {
            case ScopeStack.STATUS_EXPECT_VALUE:
                throw new IllegalStateException();
            case ScopeStack.STATUS_NEEDS_COMMA:
                output.push(',');
                break;
        }
        output.push('"' + name + '"');
    }

    @Override
    public void value(String value) {
        verifyValueWrite();
        output.push('"' + value + '"');
    }

    @Override
    public void value(boolean value) {
        verifyValueWrite();
        output.push(Boolean.toString(value));
    }

    @Override
    public void value(int value) {
        verifyValueWrite();
        output.push(Integer.toString(value));
    }

    @Override
    public void value(long value) {
        verifyValueWrite();
        output.push(Long.toString(value));
    }

    @Override
    public void value(float value) {
        verifyValueWrite();
        output.push(Float.toString(value));
    }

    @Override
    public void value(double value) {
        verifyValueWrite();
        output.push(Double.toString(value));
    }

    @Override
    public void value(BigInteger value) {
        verifyValueWrite();
        output.push(value.toString());
    }

    @Override
    public void value(BigDecimal value) {
        verifyValueWrite();
        output.push(value.toString());
    }
}
