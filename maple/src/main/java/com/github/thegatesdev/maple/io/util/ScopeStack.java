package com.github.thegatesdev.maple.io.util;

import java.util.*;

public final class ScopeStack {

    public static final byte STATUS_OK = 0;
    public static final byte STATUS_NEEDS_VALUE_SEPARATOR = 1;
    public static final byte STATUS_NEEDS_NAME_SEPARATOR = 2;
    public static final byte STATUS_NEEDS_ROOT_SEPARATOR = 3;
    public static final byte STATUS_EXPECT_NAME = 4;
    public static final byte STATUS_EXPECT_VALUE = 5;

    private static final byte SCOPE_ROOT = 0;
    private static final byte SCOPE_OBJECT = 1;
    private static final byte SCOPE_ARRAY = 2;

    private int stackIndex = -1;
    private boolean[] scopes = new boolean[2];

    private byte currentScopeID = SCOPE_ROOT;
    private boolean hasEntry = false;
    private boolean hasName = false;


    private void push(boolean scope) {
        int len = scopes.length;
        if (++stackIndex == len) { // Full, double the capacity
            len *= 2;
            scopes = Arrays.copyOf(scopes, len);
        }
        scopes[stackIndex] = scope;
        currentScopeID = toId(scope);
        hasEntry = false; // New scope, is empty
    }

    private void pop() {
        if (stackIndex > 0) {
            stackIndex--;
            currentScopeID = toId(scopes[stackIndex]);
        } else {
            currentScopeID = SCOPE_ROOT;
        }
        hasEntry = true; // Has at least 1 entry, since we just popped out of that one
    }

    private byte toId(boolean scope) {
        return scope ? SCOPE_OBJECT : SCOPE_ARRAY;
    }


    public boolean inRoot() {
        return currentScopeID == SCOPE_ROOT;
    }

    public boolean inObject() {
        return currentScopeID == SCOPE_OBJECT;
    }

    public boolean inArray() {
        return currentScopeID == SCOPE_ARRAY;
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


    public byte writeValueStatus() {
        switch (currentScopeID) {
            case SCOPE_ROOT -> {
                boolean had = hasEntry;
                hasEntry = true;
                return had ? STATUS_NEEDS_ROOT_SEPARATOR : STATUS_OK;
            }
            case SCOPE_OBJECT -> {
                if (!hasName) return STATUS_EXPECT_NAME;
                hasName = false;
                hasEntry = true;
                return STATUS_NEEDS_NAME_SEPARATOR;
            }
            case SCOPE_ARRAY -> {
                boolean had = hasEntry;
                hasEntry = true;
                return had ? STATUS_NEEDS_VALUE_SEPARATOR : STATUS_OK;
            }
        }
        return STATUS_OK;
    }

    public byte writeNameStatus() {
        if (!inObject() || hasName) return STATUS_EXPECT_VALUE;
        hasName = true;
        return hasEntry ? STATUS_NEEDS_VALUE_SEPARATOR : STATUS_OK;
    }
}
