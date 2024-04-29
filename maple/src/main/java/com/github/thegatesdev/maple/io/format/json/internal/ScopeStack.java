package com.github.thegatesdev.maple.io.format.json.internal;

import java.util.*;

public final class ScopeStack {

    public static final byte STATUS_OK = 0;
    public static final byte STATUS_NEEDS_COMMA = 1;
    public static final byte STATUS_NEEDS_COLON = 2;
    public static final byte STATUS_EXPECT_NAME = 4;
    public static final byte STATUS_EXPECT_VALUE = 5;

    private int stackIndex = -1;

    private boolean[] scopes = new boolean[2];

    private boolean currentScope = true;
    private boolean hasEntry = false;
    private boolean nameWritten = false;


    private void push(boolean scope) {
        int len = scopes.length;
        if (++stackIndex == len) {
            len *= 2;
            scopes = Arrays.copyOf(scopes, len);
        }
        scopes[stackIndex] = currentScope = scope;
        hasEntry = false;
    }

    private void pop() {
        if (stackIndex != 0) {
            stackIndex--;
            currentScope = scopes[stackIndex];
        }
        hasEntry = true; // When popping, we always know the parent has had at least one child
        // Jackson keeps track of the count, but only checks if it's higher than zero...
    }


    public void pushObject() {
        push(true);
    }

    public void pushArray() {
        push(false);
    }

    public boolean popArray() {
        if (currentScope) return false;
        pop();
        return true;
    }

    public boolean popObject() {
        if (!currentScope) return false;
        pop();
        return true;
    }


    public byte checkWriteValue() {
        if (stackIndex < 0) { // root, we don't handle that yet
            return STATUS_OK;
        }
        if (currentScope) { // Object
            if (!nameWritten) return STATUS_EXPECT_NAME;
            nameWritten = false;
            hasEntry = true;
            return STATUS_NEEDS_COLON;
        } else { // Array
            boolean had = hasEntry;
            hasEntry = true;
            return had ? STATUS_NEEDS_COMMA : STATUS_OK;
        }
    }

    public byte checkWriteName() {
        if (stackIndex < 0) { // root, we don't handle that yet
            throw new IllegalStateException("root");
        }
        if (!currentScope || nameWritten) return STATUS_EXPECT_VALUE;
        nameWritten = true;
        return hasEntry ? STATUS_NEEDS_COMMA : STATUS_OK;
    }
}
