package com.github.thegatesdev.maple.io.impl.internal;

import com.github.thegatesdev.maple.io.*;
import com.github.thegatesdev.maple.io.util.*;

import java.io.*;

public final class JsonEscapes implements Escapes {

    private static final char HIGHEST_ESCAPED_CHAR = '\\';
    private final char[] hexEscapeBuffer = HexEscaping.copyHexBuffer();

    @Override
    public boolean couldEscape(char ch) {
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
            default -> HexEscaping.writeHexOrSingle(ch, hexEscapeBuffer, output);
        }
    }
}
