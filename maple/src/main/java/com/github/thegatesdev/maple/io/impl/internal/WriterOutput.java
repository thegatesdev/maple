package com.github.thegatesdev.maple.io.impl.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;
import java.math.*;
import java.util.*;

public final class WriterOutput implements Output {

    private final Writer writer;
    private final char[] escapedStringBuffer = new char[200];

    WriterOutput(Writer writer) {
        this.writer = writer;
    }


    public static WriterOutput from(Writer writer) {
        Objects.requireNonNull(writer, "given writer is null");

        return new WriterOutput(writer);
    }


    public void raw(char[] buffer, int offset, int lenght) throws IOException {
        writer.write(buffer, offset, lenght);
    }

    public void escaped(char[] buffer, int offset, int lenght, Escapes escapes) throws IOException {
        int escapeLimit = escapes.escapeLimit();
        int head = offset;
        for (int i = head; i < lenght; i++) {
            char c = buffer[i];
            if (c > escapeLimit) continue;

            raw(buffer, head, i - head);
            head = i + 1;

            escapes.writeEscaped(this, c);
        }
        if (head < lenght) raw(buffer, head, lenght - head);
    }


    @Override
    public void raw(int character) throws IOException {
        writer.write(character);
    }

    @Override
    public void raw(String value) throws IOException {
        writer.write(value);
    }

    @Override
    public void escaped(String value, Escapes escapes) throws IOException {
        int max = escapedStringBuffer.length;
        int len = value.length();
        int index = 0;

        while (index < len) {
            int count = Math.min(len - index, max);
            value.getChars(index, index + count, escapedStringBuffer, 0);
            escaped(escapedStringBuffer, 0, count, escapes);
            index += count;
        }
    }


    @Override
    public void value(boolean value) throws IOException {
        raw(value ? "true" : "false");
    }

    @Override
    public void value(short value) throws IOException {
        raw(Integer.toString(value));
    }

    @Override
    public void value(int value) throws IOException {
        raw(Integer.toString(value));
    }

    @Override
    public void value(long value) throws IOException {
        raw(Long.toString(value));
    }

    @Override
    public void value(float value) throws IOException {
        raw(Float.toString(value));
    }

    @Override
    public void value(double value) throws IOException {
        raw(Double.toString(value));
    }

    @Override
    public void value(BigInteger value) throws IOException {
        raw(value.toString(10));
    }

    @Override
    public void value(BigDecimal value) throws IOException {
        raw(value.toString());
    }

    @Override
    public void nullValue() throws IOException {
        raw("null");
    }
}
