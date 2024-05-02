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
    public void write(char[] buf, int off, int len) {
        try {
            writer.write(buf, off, len);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void writeEscaped(char[] buf, int off, int len, int[] escapes) {
        if (len > MAX_STRING_SIZE)
            throw new IllegalArgumentException("string longer than " + MAX_STRING_SIZE);
        int escapeLen = escapes.length;

        int index = 0;
        int head = 0;

        while (index < len) {
            char c;
            while (true) {
                c = buf[index];
                if (c < escapeLen && escapes[c] != 0) break;
                if (++index >= len) {
                    write(buf, head, index - head);
                    return;
                }
            }
            write(buf, head, index - head);
            head = ++index;

            write('\\');
            write(c);
        }
    }

    @Override
    public void writeEscaped(String s, int[] escapes) {
        char[] buf = stringBuffer();
        int len = s.length();
        if (len > MAX_STRING_SIZE)
            throw new IllegalArgumentException("string longer than " + MAX_STRING_SIZE);
        s.getChars(0, len, buf, 0);
        writeEscaped(buf, 0, len, escapes);
    }
}
