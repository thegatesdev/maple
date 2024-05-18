package com.github.thegatesdev.maple.io.impl.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;
import java.math.*;

public final class WriterOutput implements Output {

    private static final int ESCAPED_STRING_BUFFER_SIZE = 200;

    private final Writer writer;

    private char[] escapedStringBuffer;

    public WriterOutput(Writer writer) {
        this.writer = writer;
    }


    private char[] getEscapedStringBuffer() {
        if (escapedStringBuffer == null) {
            escapedStringBuffer = new char[ESCAPED_STRING_BUFFER_SIZE];
        }
        return escapedStringBuffer;
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
        char[] buf = getEscapedStringBuffer();
        int max = buf.length;
        int len = s.length();
        int index = 0;

        while (index < len) {
            int count = Math.min(len - index, max);
            s.getChars(index, index + count, buf, 0);
            escaped(buf, 0, count, escapes);
            index += count;
        }
    }


    @Override
    public void value(boolean b) throws IOException {
        if (b) {
            raw('t');
            raw('r');
            raw('u');
            raw('e');
        } else {
            raw('f');
            raw('a');
            raw('l');
            raw('s');
            raw('e');
        }
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
        raw('n');
        raw('u');
        raw('l');
        raw('l');
    }
}
