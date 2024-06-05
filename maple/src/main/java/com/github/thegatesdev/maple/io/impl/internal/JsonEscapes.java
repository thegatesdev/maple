package com.github.thegatesdev.maple.io.impl.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;

public final class JsonEscapes implements Escapes {

    private static final char HIGHEST_ESCAPED_CHAR = '\\';

    private static final char HIGHEST_HEX_CHAR = 0x1f;
    private static final char[] HEX_ESCAPES = buildHexEscapes();

    private static char[] buildHexEscapes() {
        char[] hexChars = "0123456789ABCDEF".toCharArray();
        char[] escapes = new char[(HIGHEST_HEX_CHAR + 1) * 2];
        for (int i = 0; i < escapes.length; i += 2) {
            escapes[i] = hexChars[i >> 4];
            escapes[i + 1] = hexChars[i & 0xF];
        }
        return escapes;
    }


    private final char[] hexEscapeBuffer = new char[]{
        '\\', 'u', '0', '0', 0, 0
    };


    JsonEscapes() {
    }

    public static JsonEscapes create() {
        return new JsonEscapes();
    }


    @Override
    public boolean couldEscape(char ch) {
        return ch <= HIGHEST_ESCAPED_CHAR;
    }

    @Override
    public boolean writeEscaped(Output output, char ch) throws IOException {
        if (ch <= HIGHEST_HEX_CHAR) {
            // 6 character escape
            int index = ch * 2;
            hexEscapeBuffer[4] = HEX_ESCAPES[index];
            hexEscapeBuffer[5] = HEX_ESCAPES[index + 1];
            output.raw(hexEscapeBuffer, 0, 6);
            return true;
        } else {
            // 2 character escape
            switch (ch) {
                case '\u2028', '\u2029': // Javascript failures, TODO make a setting
                case '\\', '\"':
                    output.raw('\\');
                    output.raw(ch);
                    return true;
            }
            // Character should not be escaped, it just made it through chouldEscape
            return false;
        }
    }
}
