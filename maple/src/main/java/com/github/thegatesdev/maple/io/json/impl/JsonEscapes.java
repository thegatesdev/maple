package com.github.thegatesdev.maple.io.json.impl;

import java.util.*;

public final class JsonEscapes {

    public static final char HIGHEST_ESCAPED_CHAR = '\\';
    public static final char[] HEX_ESCAPES = buildHexEscapes();
    public static final char HIGHEST_HEX_CHAR = 0x1f;

    private static final char[] ESCAPE_BUFFER = new char[]{
        '\\', 0, // 0, 1: 2 character escapes
        '\\', 'u', '0', '0', 0, 0, // 2, 7: 6 character escapes
    };

    private static char[] buildHexEscapes() {
        char[] hexChars = "0123456789ABCDEF".toCharArray();
        char[] escapes = new char[(HIGHEST_HEX_CHAR + 1) * 2];
        for (int i = 0; i < escapes.length; i += 2) {
            escapes[i] = hexChars[i >> 4];
            escapes[i + 1] = hexChars[i & 0xF];
        }
        return escapes;
    }


    public static char[] copyEscapeBuffer() {
        return Arrays.copyOf(ESCAPE_BUFFER, ESCAPE_BUFFER.length);
    }

    public static void putHexEscaping(char[] escapeBuffer, char toEscape) {
        int index = toEscape * 2;
        escapeBuffer[6] = HEX_ESCAPES[index];
        escapeBuffer[7] = HEX_ESCAPES[index + 1];
    }


    private JsonEscapes() {
    }
}
