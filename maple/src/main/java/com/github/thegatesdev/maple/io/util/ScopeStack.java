package com.github.thegatesdev.maple.io.util;

import java.util.*;

public final class ScopeStack {

    public static final byte STATUS_OK = 0;
    public static final byte STATUS_NEEDS_VALUE_SEPARATOR = 1;
    public static final byte STATUS_NEEDS_NAME_SEPARATOR = 2;
    public static final byte STATUS_EXPECT_NAME = 4;
    public static final byte STATUS_EXPECT_VALUE = 5;

    private int stackIndex = -1;

    private boolean[] scopes = new boolean[2];

    private boolean currentScope = true;
    private boolean hasEntry = false;
    private boolean nameWritten = false;


    private void push(boolean scope) {
        int len = scopes.length;
        if (++stackIndex == len) { // Full, double the capacity
            len *= 2;
            scopes = Arrays.copyOf(scopes, len);
        }
        scopes[stackIndex] = currentScope = scope;
        hasEntry = false; // New scope, is empty
    }

    private void pop() {
        if (stackIndex != 0) {
            stackIndex--;
            currentScope = scopes[stackIndex];
        }
        hasEntry = true; // Has at least 1 entry, since we just popped out of that one
    }


    public void pushObject() {
        push(true);
    }

    public void pushArray() {
        push(false);
    }

    public boolean popArray() {
        if (!inArray()) return false;
        pop();
        return true;
    }

    public boolean popObject() {
        if (!inObject()) return false;
        pop();
        return true;
    }

    public boolean inArray() {
        return !currentScope;
    }

    public boolean inObject() {
        return currentScope;
    }


    public byte writeValueStatus() {
        if (stackIndex < 0) { // root, we don't handle that yet
            return STATUS_OK; // Don't throw, this needs to be called in root for the initial root object
        }
        if (inObject()) { // Object
            if (!nameWritten) return STATUS_EXPECT_NAME;
            nameWritten = false;
            hasEntry = true;
            return STATUS_NEEDS_NAME_SEPARATOR;
        } else { // Array
            boolean had = hasEntry;
            hasEntry = true;
            return had ? STATUS_NEEDS_VALUE_SEPARATOR : STATUS_OK;
        }
    }

    public byte writeNameStatus() {
        if (stackIndex < 0) { // root, we don't handle that yet
            throw new IllegalStateException("root");
        }
        if (inArray() || nameWritten) return STATUS_EXPECT_VALUE;
        nameWritten = true;
        return hasEntry ? STATUS_NEEDS_VALUE_SEPARATOR : STATUS_OK;
    }
}
