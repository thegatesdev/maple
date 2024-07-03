package com.github.thegatesdev.maple.io.json.impl;

import com.github.thegatesdev.maple.annotation.internal.*;
import com.github.thegatesdev.maple.io.*;

import java.util.*;

@ValueClassCandidate
public final class JsonWriteContext {

    private static final char[] BUF_TRUE = "true".toCharArray();
    private static final char[] BUF_FALSE = "false".toCharArray();
    private static final char[] BUF_NULL = "null".toCharArray();

    private static final char HIGHEST_HEX_CHAR = 0x1f;

    private static final char[] HEX_ESCAPES = buildHexEscapes();
    private static final char[] ESCAPE_BUFFER = buildEscapeBuffer();
    private static final char[] TWO_CHAR_ESCAPES = buildTwoCharEscapes();

    private static char[] buildHexEscapes() {
        char[] hexChars = "0123456789ABCDEF".toCharArray();
        char[] escapes = new char[(HIGHEST_HEX_CHAR) * 2];
        for (int i = 0, c = 0; i < escapes.length; i += 2, c++) {
            escapes[i] = hexChars[c >> 4];
            escapes[i + 1] = hexChars[c & 0xF];
        }
        return escapes;
    }

    private static char[] buildEscapeBuffer() {
        return new char[]{
            '\\', 0, // 0, 1: 2 character escapes
            '\\', 'u', '0', '0', 0, 0, // 2, 7: 6 character escapes
        };
    }

    private static char[] buildTwoCharEscapes() {
        char[] buf = new char[14];
        buf['\b'] = 'b';
        buf['\t'] = 't';
        buf['\n'] = 'n';
        buf['\f'] = 'f';
        buf['\r'] = 'r';
        return buf;
    }


    private final char[] escapeBuffer;
    private final char[] largeStringBuffer;


    public JsonWriteContext() {
        escapeBuffer = Arrays.copyOf(ESCAPE_BUFFER, ESCAPE_BUFFER.length);
        largeStringBuffer = new char[200]; // TODO reuse buffers (via interface, so it can be dummied) https://stackoverflow.com/questions/1955322/at-what-point-is-it-worth-reusing-arrays-in-java
    }


    public void writeEscaped(Output output, String value) {
        char[] buf = largeStringBuffer;
        int max = buf.length;
        int len = value.length();
        int index = 0;

        // The string can be any size, copy parts to a buffer and write from that
        while (index < len) {
            int count = Math.min(len - index, max);

            value.getChars(index, index += count, buf, 0);
            writeEscaped(output, buf, count);
        }
    }

    public void writeEscaped(Output output, char[] buffer, int lenght) {
        int head = 0;

        for (int i = head; i < lenght; i++) {
            char currentChar = buffer[i];
            switch (currentChar) {
                case '\"', '\\', '/':
                    output.raw(buffer, head, i - head); // Flush
                    writeTwoChar(output, currentChar);
                    head = i + 1;
                    break;
                case '\b', '\t', '\n', '\f', '\r':
                    output.raw(buffer, head, i - head); // Flush
                    writeTwoChar(output, TWO_CHAR_ESCAPES[currentChar]);
                    head = i + 1;
                    break;
                default:
                    if (currentChar > JsonWriteContext.HIGHEST_HEX_CHAR) continue;

                    output.raw(buffer, head, i - head); // Flush
                    writeHex(output, currentChar);
                    head = i + 1;
            }
        }

        if (head < lenght) {
            // Write the leftover data after the last escaped character
            output.raw(buffer, head, lenght - head);
        }
    }

    public void writeLiteralTrue(Output output) {
        output.raw(BUF_TRUE, 0, 4);
    }

    public void writeLiteralFalse(Output output) {
        output.raw(BUF_FALSE, 0, 5);
    }

    public void writeLiteralNull(Output output) {
        output.raw(BUF_NULL, 0, 4);
    }


    private void writeTwoChar(Output output, char c) {
        escapeBuffer[1] = c;
        output.raw(escapeBuffer, 0, 2);
    }

    private void writeHex(Output output, char c) {
        int index = c * 2;
        escapeBuffer[6] = HEX_ESCAPES[index];
        escapeBuffer[7] = HEX_ESCAPES[index + 1];
        output.raw(escapeBuffer, 2, 6);
    }
}

/*
Copyright 2024 Timar Karels

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
