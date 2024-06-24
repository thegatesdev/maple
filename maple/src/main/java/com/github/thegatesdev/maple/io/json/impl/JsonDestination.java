package com.github.thegatesdev.maple.io.json.impl;

import com.github.thegatesdev.maple.exception.*;
import com.github.thegatesdev.maple.io.*;
import com.github.thegatesdev.maple.io.json.util.*;

import java.io.*;
import java.math.*;

public class JsonDestination implements Destination {

    private static final char[] BUF_TRUE = "true".toCharArray();
    private static final char[] BUF_FALSE = "false".toCharArray();
    private static final char[] BUF_NULL = "null".toCharArray();

    private final JsonScopes jsonScopes = JsonScopes.root();
    private final Writer writer;

    private final char[] escapedStringBuffer = new char[200];
    private final char[] escapeBuffer = JsonEscapes.copyEscapeBuffer();

    private JsonDestination(Writer writer) {
        this.writer = writer;
    }


    public static JsonDestination of(Writer writer) {
        return new JsonDestination(writer);
    }


    private void raw(int character) {
        try {
            writer.write(character);
        } catch (IOException e) {
            throw new OutputException(e);
        }
    }

    private void raw(String value) {
        try {
            writer.write(value);
        } catch (IOException e) {
            throw new OutputException(e);
        }
    }

    public void raw(char[] buffer, int offset, int length) {
        try {
            writer.write(buffer, offset, length);
        } catch (IOException e) {
            throw new OutputException(e);
        }
    }

    private void escapedQuoted(String value) {
        char[] buf = escapedStringBuffer;
        int max = buf.length;
        int len = value.length();
        int index = 0;

        raw('"');
        // The string can be any size, copy parts to a buffer and write from that
        while (index < len) {
            int count = Math.min(len - index, max);

            value.getChars(index, index + count, buf, 0);
            escaped(buf, count);

            index += count;
        }
        raw('"');
    }

    private void escaped(char[] buffer, int lenght) {
        int head = 0;
        char[] escapeBuf = escapeBuffer;

        for (int i = head; i < lenght; i++) {
            char currentChar = buffer[i];
            if (currentChar > JsonEscapes.HIGHEST_ESCAPED_CHAR) continue;

            if (currentChar <= JsonEscapes.HIGHEST_HEX_CHAR) {
                // 6 character escape

                // Flush
                raw(buffer, head, i - head);
                head = i + 1;

                JsonEscapes.putHexEscaping(escapeBuf, currentChar);
                raw(escapeBuf, 2, 6);
                continue;
            }

            switch (currentChar) {
                // 2 character escape

                // case '\u2028', '\u2029': // Javascript failures, TODO make a setting
                case '\\', '\"':
                    // Flush
                    raw(buffer, head, i - head);
                    head = i + 1;

                    escapeBuffer[1] = currentChar;
                    raw(escapeBuffer, 0, 2);
                    break;
            }
        }

        if (head < lenght) {
            // Write the leftover data after the last escaped character
            raw(buffer, head, lenght - head);
        }
    }


    private void verifyWriteValue() {
        switch (jsonScopes.beforeWriteValue()) {
            case NeedsNameSeparator -> raw(':');
            case NeedsRootSeparator -> raw(' ');
            case NeedsValueSeparator -> raw(',');
            case ExpectedName -> throw new IllegalStateException("Expected name before value");
        }
    }

    private void verifyWriteName() {
        switch (jsonScopes.beforeWriteName()) {
            case ReadyAfterValueSeparator -> raw(',');
            case ExpectedValue -> throw new IllegalStateException("Expected value before name");
        }
    }

    private void verifyCloseScope(JsonScope scope) {
        if (!jsonScopes.pop(scope)) throw new IllegalStateException("Not in " + scope.name() + " scope");
    }


    @Override
    public void openObject() {
        verifyWriteValue();
        jsonScopes.push(JsonScope.OBJECT);
        raw('{');
    }

    @Override
    public void closeObject() {
        verifyCloseScope(JsonScope.OBJECT);
        raw('}');
    }

    @Override
    public void openArray() {
        verifyWriteValue();
        jsonScopes.push(JsonScope.ARRAY);
        raw('[');
    }

    @Override
    public void closeArray() {
        verifyCloseScope(JsonScope.ARRAY);
        raw(']');
    }


    @Override
    public void name(String name) {
        verifyWriteName();
        escapedQuoted(name);
    }


    @Override
    public void value(String value) {
        verifyWriteValue();
        escapedQuoted(value);
    }

    @Override
    public void value(boolean value) {
        verifyWriteValue();
        raw(value ? BUF_TRUE : BUF_FALSE, 0, 5);
    }

    @Override
    public void value(int value) {
        verifyWriteValue();
        raw(Integer.toString(value, 10));
    }

    @Override
    public void value(long value) {
        verifyWriteValue();
        raw(Long.toString(value));
    }

    @Override
    public void value(float value) {
        verifyWriteValue();
        raw(Float.toString(value));
    }

    @Override
    public void value(double value) {
        verifyWriteValue();
        raw(Double.toString(value));
    }

    @Override
    public void value(BigInteger value) {
        verifyWriteValue();
        raw(value.toString(10));
    }

    @Override
    public void value(BigDecimal value) {
        verifyWriteValue();
        raw(value.toString());
    }

    @Override
    public void valueNull() {
        verifyWriteValue();
        raw(BUF_NULL, 0, 4);
    }


    @Override
    public void close() throws IOException {
        writer.close();
    }
}
