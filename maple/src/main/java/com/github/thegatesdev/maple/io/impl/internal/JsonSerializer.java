package com.github.thegatesdev.maple.io.impl.internal;

import com.github.thegatesdev.maple.io.*;
import com.github.thegatesdev.maple.io.util.*;

import java.io.*;
import java.math.*;

public final class JsonSerializer implements Serializer, Escapes {

    private final Output output;
    private final ScopeStack scope = new ScopeStack();

    public JsonSerializer(Output output) {
        this.output = output;
    }


    private void verifyValueWrite() throws IOException {
        char write;
        switch (scope.writeValueStatus()) {
            case ScopeStack.STATUS_OK:
            default:
                return;
            case ScopeStack.STATUS_NEEDS_VALUE_SEPARATOR:
                write = ',';
                break;
            case ScopeStack.STATUS_NEEDS_NAME_SEPARATOR:
                write = ':';
                break;
            case ScopeStack.STATUS_NEEDS_ROOT_SEPARATOR:
                write = ' ';
                break;
            case ScopeStack.STATUS_EXPECT_NAME:
                throw new IllegalStateException("expected name before value");
        }
        output.raw(write);
    }


    @Override
    public void openObject() throws IOException {
        verifyValueWrite();
        scope.pushObject();
        output.raw('{');
    }

    @Override
    public void closeObject() throws IOException {
        if (!scope.popObject()) throw new IllegalStateException("scope != object"); // TODO figure out errors
        output.raw('}');
    }

    @Override
    public void openArray() throws IOException {
        verifyValueWrite();
        scope.pushArray();
        output.raw('[');
    }

    @Override
    public void closeArray() throws IOException {
        if (!scope.popArray()) throw new IllegalStateException("scope != array"); // TODO figure out errors
        output.raw(']');
    }


    @Override
    public void name(String name) throws IOException {
        byte status = scope.writeNameStatus();
        switch (status) {
            case ScopeStack.STATUS_EXPECT_VALUE:
                throw new IllegalStateException("expected value");
            case ScopeStack.STATUS_NEEDS_VALUE_SEPARATOR:
                output.raw(',');
                break;
        }
        output.raw('"');
        output.escaped(name, this);
        output.raw('"');
    }

    @Override
    public void value(String value) throws IOException {
        verifyValueWrite();
        output.raw('"');
        output.escaped(value, this);
        output.raw('"');
    }

    @Override
    public void value(boolean value) throws IOException {
        verifyValueWrite();
        output.value(value);
    }

    @Override
    public void value(int value) throws IOException {
        verifyValueWrite();
        output.value(value);
    }

    @Override
    public void value(long value) throws IOException {
        verifyValueWrite();
        output.value(value);
    }

    @Override
    public void value(float value) throws IOException {
        verifyValueWrite();
        output.value(value);
    }

    @Override
    public void value(double value) throws IOException {
        verifyValueWrite();
        output.value(value);
    }

    @Override
    public void value(BigInteger value) throws IOException {
        verifyValueWrite();
        output.value(value);
    }

    @Override
    public void value(BigDecimal value) throws IOException {
        verifyValueWrite();
        output.value(value);
    }

    @Override
    public void nullValue() throws IOException {
        verifyValueWrite();
        output.nullValue();
    }


    @Override
    public int escapeLimit() {
        return '\\';
    }

    @Override
    public void writeEscaped(Output output, char ch) throws IOException {
        switch (ch) {
            case '"':
                output.raw('\\');
                output.raw(ch);
                break;
            case '\\':
                output.raw('\\');
                output.raw('\\');
                break;
        }
    }
}
