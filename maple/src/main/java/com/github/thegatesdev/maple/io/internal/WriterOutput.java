package com.github.thegatesdev.maple.io.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;
import java.math.*;

public final class WriterOutput implements Output {

    private final Writer writer;
    private final char[] intermediaryBuf;
    private final int intermediaryLen;


    public WriterOutput(Writer writer) {
        this.writer = writer;
        this.intermediaryBuf = new char[200];
        this.intermediaryLen = intermediaryBuf.length;
    }


    private int checkAndFillIntermediary(String s) {
        int len = s.length();
        if (len > intermediaryLen) throw new IllegalArgumentException("string lenght > " + intermediaryLen);

        s.getChars(0, len, intermediaryBuf, 0);
        return len;
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
    public void raw(String s) throws IOException {
        // We do not use Writer#write(String) since that creates a new array every time
        int len = checkAndFillIntermediary(s);
        raw(intermediaryBuf, 0, len);
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
        // We do not use Writer#write(String) since that creates a new array every time
        int len = checkAndFillIntermediary(s);
        escaped(intermediaryBuf, 0, len, escapes);
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
