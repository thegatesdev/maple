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


    @Override
    public void raw(int ch) throws IOException {
        writer.write(ch);
    }

    @Override
    public void raw(char[] buf, int offset, int len) throws IOException {
        writer.write(buf, offset, len);
    }

    @Override
    public void raw(String string) throws IOException {
        writer.write(string);
    }

    @Override
    public void escaped(char[] buf, int offset, int len, Escapes escapes) throws IOException {
        int escapeLimit = escapes.escapeLimit();
        int head = offset;
        for (int i = head; i < len; i++) {
            char c = buf[i];
            if (c > escapeLimit) continue;

            raw(buf, head, i - head);
            head = i + 1;

            escapes.writeEscaped(this, c);
        }
        if (head < len) raw(buf, head, len - head);
    }

    @Override
    public void escaped(String s, Escapes escapes) throws IOException {
        int max = escapedStringBuffer.length;
        int len = s.length();
        int index = 0;

        while (index < len) {
            int count = Math.min(len - index, max);
            s.getChars(index, index + count, escapedStringBuffer, 0);
            escaped(escapedStringBuffer, 0, count, escapes);
            index += count;
        }
    }


    @Override
    public void value(boolean b) throws IOException {
        raw(b ? "true" : "false");
    }

    @Override
    public void value(short i) throws IOException {
        raw(Integer.toString(i));
    }

    @Override
    public void value(int i) throws IOException {
        raw(Integer.toString(i));
    }

    @Override
    public void value(long l) throws IOException {
        raw(Long.toString(l));
    }

    @Override
    public void value(float f) throws IOException {
        raw(Float.toString(f));
    }

    @Override
    public void value(double d) throws IOException {
        raw(Double.toString(d));
    }

    @Override
    public void value(BigInteger bI) throws IOException {
        raw(bI.toString(10));
    }

    @Override
    public void value(BigDecimal bD) throws IOException {
        raw(bD.toString());
    }

    @Override
    public void nullValue() throws IOException {
        raw("null");
    }
}
