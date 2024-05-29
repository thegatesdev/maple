package com.github.thegatesdev.maple.io.impl.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;

public final class JsonEscapes implements Escapes {

    private static final String[] HEX_ESCAPES;

    static {
        HEX_ESCAPES = new String[32]; // TODO make method
        for (int i = 0; i <= 0x1f; i++) {
            HEX_ESCAPES[i] = String.format("\\u%04x", i);
        }
    }

    @Override
    public int escapeLimit() {
        return 92; // TODO make magic values named constants
    }

    @Override
    public void writeEscaped(Output output, char ch) throws IOException {
        switch (ch) {
            case '\\' -> {
                output.raw('\\');
                output.raw('\\');
            }
            case '\"' -> {
                output.raw('\\');
                output.raw('\"');
            }
            case '\u2028' -> { // Javascript failures, TODO make a setting
                output.raw('\\');
                output.raw('\u2028');
            }
            case '\u2029' -> {
                output.raw('\\');
                output.raw('\u2029');
            }
            default -> {
                if (ch <= 0x1f) {
                    output.raw(HEX_ESCAPES[ch]);
                }
            }
        }
    }
}
