package io.github.thegatesdev.maple.io.json.impl;

import io.github.thegatesdev.maple.exception.*;
import io.github.thegatesdev.maple.io.*;

import java.util.*;

public final class JsonWriteContext implements AutoCloseable {

    private static final char[] BUF_TRUE = "true".toCharArray();
    private static final char[] BUF_FALSE = "false".toCharArray();
    private static final char[] BUF_NULL = "null".toCharArray();

    private static final char HIGHEST_HEX_CHAR = 0x1f;

    private static final char[] HEX_ESCAPES = buildHexEscapes();
    private static final char[] TWO_CHAR_ESCAPES = buildTwoCharEscapes();


    private final char[] escapeBuf;
    private final char[] largeStringBuf;

    // TODO store some object *interface* managing buffer reuse (optional, not present means not managed)
    private boolean closed = false;

    public JsonWriteContext(char[] escapeBuf, char[] largeStringBuf) {
        this.escapeBuf = escapeBuf;
        this.largeStringBuf = largeStringBuf;
    }

    public JsonWriteContext() {
        this(buildEscapeBuffer(), new char[200]);
    }


    private void ensureOpen() {
        if (closed) {
            throw new OutputException("JsonWriteContext closed");
        }
    }

    @Override
    public void close() {
        if (!closed) {
            closed = true;
        }
    }


    public void writeEscaped(Output output, String value) {
        int max = largeStringBuf.length;
        int len = value.length();
        int index = 0;

        // The string can be any size, copy parts to a buffer and write from that
        while (index < len) {
            int count = Math.min(len - index, max);

            value.getChars(index, index += count, largeStringBuf, 0);
            writeEscaped(output, escapeBuf, largeStringBuf, 0, count);
        }
    }

    public void writeEscaped(Output output, char[] inputBuffer, int offset, int lenght) {
        Objects.checkFromIndexSize(offset, lenght, inputBuffer.length);
        writeEscaped(output, escapeBuf, inputBuffer, offset, lenght);
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


    private static void writeEscaped(Output output, char[] escapeBuf, char[] inputBuffer, int offset, int lenght) {
        int head = offset;

        for (int i = head; i < lenght; i++) {
            char currentChar = inputBuffer[i];
            switch (currentChar) {
                default -> {
                    if (currentChar > HIGHEST_HEX_CHAR) continue;

                    output.raw(inputBuffer, head, i - head); // Flush
                    writeHex(output, escapeBuf, currentChar);
                }
                case '\"', '\\', '/' -> {
                    output.raw(inputBuffer, head, i - head); // Flush
                    writeTwoChar(output, escapeBuf, currentChar);
                }
                case '\b', '\t', '\n', '\f', '\r' -> {
                    output.raw(inputBuffer, head, i - head); // Flush
                    writeTwoChar(output, escapeBuf, TWO_CHAR_ESCAPES[currentChar]);
                }
            }
            head = i + 1;
        }

        if (head < lenght) {
            // Write the leftover data after the last escaped character
            output.raw(inputBuffer, head, lenght - head);
        }
    }

    private static void writeTwoChar(Output output, char[] escapeBuffer, char c) {
        escapeBuffer[1] = c;
        output.raw(escapeBuffer, 0, 2);
    }

    private static void writeHex(Output output, char[] escapeBuffer, char c) {
        int index = c * 2;
        escapeBuffer[6] = HEX_ESCAPES[index];
        escapeBuffer[7] = HEX_ESCAPES[index + 1];
        output.raw(escapeBuffer, 2, 6);
    }


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
