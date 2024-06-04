package com.github.thegatesdev.maple.io.impl.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;

public final class JsonEscapes implements Escapes {

    private static final char HIGHEST_HEX_CHAR = 0x1f;
    private static final char HIGHEST_ESCAPED_CHAR = '\\';

    private static final char[] HEX_ESCAPES = buildHexEscapes();
    private static final char[] HEX_ESCAPE_BUFFER = new char[]{
        '\\', 'u', '0', '0', 0, 0
    };


    private static char[] buildHexEscapes() {
        var escapes = new char[(HIGHEST_HEX_CHAR + 1) * 2];
        for (int i = 0; i < escapes.length; i += 2) {
            int lo = i & 0xFF;
            escapes[i] = (char) (lo >> 4);
            escapes[i + 1] = (char) (lo & 0xF);
        }
        return escapes;
    }

    private static int hexEscapeIndex(char ch) {
        return ch * 2;
    }


    @Override
    public boolean shouldEscape(char ch) {
        return ch <= HIGHEST_ESCAPED_CHAR;
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
                if (ch <= HIGHEST_HEX_CHAR) {
                    int index = hexEscapeIndex(ch);
                    HEX_ESCAPE_BUFFER[4] = HEX_ESCAPES[index];
                    HEX_ESCAPE_BUFFER[5] = HEX_ESCAPES[index + 1];
                    output.raw(HEX_ESCAPE_BUFFER, 6, 0);
                }
            }
        }
    }
}
