package com.github.thegatesdev.maple.io.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;

public final class WriterOutput implements Output {

    private final Writer writer;
    private char[] stringBuffer;
    private static final int MAX_STRING_SIZE = 200;

    public WriterOutput(Writer writer) {
        this.writer = writer;
    }


    private char[] stringBuffer() {
        if (stringBuffer == null)
            stringBuffer = new char[MAX_STRING_SIZE];
        return stringBuffer;
    }


    @Override
    public void write(int c) {
        try {
            writer.write(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(String s) {
        try {
            writer.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeEscaped(String s, int[] escapes) {
        int len = s.length();
        if (len > MAX_STRING_SIZE)
            throw new IllegalArgumentException("string longer than " + MAX_STRING_SIZE);
        int maxEscapes = escapes.length;
        char[] buf = stringBuffer();
        s.getChars(0, len, buf, 0);

        int index = 0;

        while (index < len) {

            char c;
            do {
                c = buf[index++];
            } while (c >= maxEscapes || escapes[c] == 0);

            write('\\');
            write(c);
        }
    }
}
