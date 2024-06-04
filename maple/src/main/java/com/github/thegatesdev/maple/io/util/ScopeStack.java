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
    /**
     * Scopes represented as a BitSet.
     * A scope is either an array or an object.
     * The root scope only appears before the start of the stack.
     */
    private BitSet scopes = new BitSet();

    private byte currentScopeID = SCOPE_ROOT;
    private boolean hasEntry = false;
    private boolean hasName = false; // Only in object scope, expect a value after a name


    private void ensureCapacity(int index) {
        int size = scopes.size();
        if (index >= size) { // Full, double the capacity
            size *= 2;
            BitSet result = new BitSet(size);
            result.or(scopes);
            scopes = result;
        }
    }

    private void setScope(int index, boolean scope) {
        if (scope) scopes.set(index);
        else scopes.clear(index);
    }


    private void push(boolean scope) {
        ensureCapacity(++stackIndex);
        setScope(stackIndex, scope);

        currentScopeID = toId(scope);
        hasEntry = false; // New scope, is empty
        hasName = false;
    }

    private void pop() {
        if (stackIndex > 0) {
            stackIndex--;
            currentScopeID = toId(scopes.get(stackIndex));
        } else {
            currentScopeID = SCOPE_ROOT;
        }
        hasEntry = true; // Has at least 1 entry, since we just popped out of that one
        hasName = false;
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
        return switch (currentScopeID) {
            case SCOPE_ROOT:
                if (hasEntry) yield STATUS_NEEDS_ROOT_SEPARATOR;
                hasEntry = true;
                yield STATUS_OK; // First entry, no separator
            case SCOPE_OBJECT:
                if (!hasName) yield STATUS_EXPECT_NAME;
                hasName = false; // Wrote value, new entry requires name again
                hasEntry = true;
                yield STATUS_NEEDS_NAME_SEPARATOR;
            case SCOPE_ARRAY:
                if (hasEntry) yield STATUS_NEEDS_VALUE_SEPARATOR;
                hasEntry = true;
                yield STATUS_OK; // First entry, no separator
            default:
                yield STATUS_OK;
        };
    }

    public byte writeNameStatus() {
        if (!inObject() || hasName) return STATUS_EXPECT_VALUE;
        hasName = true;
        return hasEntry ? STATUS_NEEDS_VALUE_SEPARATOR : STATUS_OK;
    }
}
