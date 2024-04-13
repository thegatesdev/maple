package com.github.thegatesdev.maple.io.json;

import com.github.thegatesdev.maple.io.Token;
import com.github.thegatesdev.maple.io.Tokener;

import java.io.IOException;
import java.io.Reader;

public final class JsonTokener implements Tokener {

    private final Reader reader;

    public JsonTokener(Reader reader) {
        this.reader = reader;
    }


    private int nextCharacter() throws IOException {
        return reader.read();
    }

    private void skipAfterNewLine() throws IOException {
        while (true) {
            switch (nextCharacter()) {
                case -1, '\n', '\r':
                    return;
            }
        }
    }

    private boolean skipAfterFind(String toFind) throws IOException {
        int length = toFind.length();
        if (length == 0) return true;
        int first = toFind.charAt(0);

        while (true) {
            int next = nextCharacter();
            if (next == -1) return false;
            if (next == first) {
                for (int i = 1; i < length; i++) {
                    next = nextCharacter();
                    if (next == -1) return false;
                    if (next != toFind.charAt(i)) break;
                }
                return true;
            }
        }
    }

    public int nextRelevantCharacter() throws IOException {
        while (true) {
            int next = nextCharacter();

            switch (next) {
                case -1:
                    return -1;// No more characters left
                case '\n', ' ', '\r', '\t', '\ufeff': // Skip control characters
                    break;
                case '/': // '//' or '/* */' comment
                    next = nextCharacter();
                    switch (next) {
                        case -1: // No more characters after single /
                            return '/';
                        case '/':
                            skipAfterNewLine();
                            continue;
                        case '*':
                            if (!skipAfterFind("*/")) {
                                throw new RuntimeException("Unterminated comment");
                            }
                            break;
                        default:
                            break;
                    }
                case '#': // Hash
                    skipAfterNewLine();
                    break;
                default:
                    return next;
            }
        }
    }


    @Override
    public Token next() {
        return null;
    }

    @Override
    public void skipObject() {

    }

    @Override
    public void skipArray() {

    }

    @Override
    public void skipField() {

    }

    @Override
    public String nextName() {
        return null;
    }

    @Override
    public String nextString() {
        return null;
    }

    @Override
    public int nextInt() {
        return 0;
    }

    @Override
    public long nextLong() {
        return 0;
    }

    @Override
    public double nextDouble() {
        return 0;
    }

    @Override
    public boolean nextBoolean() {
        return false;
    }

    @Override
    public void nextNull() {

    }
}
