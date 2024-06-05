package com.github.thegatesdev.maple.io.util;

import com.github.thegatesdev.maple.io.*;

import java.io.*;
import java.util.*;

public final class HexEscaping {

    private static final char HIGHEST_HEX_CHAR = 0x1f;

    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();
    private static final char[] HEX_ESCAPES = buildHexEscapes();
    private static final char[] HEX_ESCAPE_BUFFER = new char[]{
        '\\', 'u', '0', '0', 0, 0
    };

    private static char[] buildHexEscapes() {
        int size = (HIGHEST_HEX_CHAR + 1) * 2;
        char[] escapes = new char[size];
        for (int i = 0; i < size; i += 2) {
            escapes[i] = HEX_CHARS[i >> 4];
            escapes[i + 1] = HEX_CHARS[i & 0xF];
        }
        return escapes;
    }

    public static char[] copyHexBuffer() {
        return Arrays.copyOf(HEX_ESCAPE_BUFFER, HEX_ESCAPE_BUFFER.length);
    }

    public static void writeHexOrSingle(char ch, char[] escapeBuffer, Output output) throws IOException {
        if (ch <= HIGHEST_HEX_CHAR) { // TODO Convert this to an instance that manages the escape buffer
            int idx = ch * 2;
            escapeBuffer[4] = HEX_ESCAPES[idx];
            escapeBuffer[5] = HEX_ESCAPES[idx + 1];
            output.raw(escapeBuffer, 0, 6);
        } else {
            output.raw(ch);
        }
    }
}
