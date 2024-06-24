package com.github.thegatesdev.maple.io.json.util;

import java.util.*;

import static com.github.thegatesdev.maple.io.json.util.JsonScope.*;

public final class JsonScopes {

    private final Deque<JsonScope> scopes = new ArrayDeque<>();
    private JsonScope currentScope = ROOT;

    private boolean hasEntry = false;
    private boolean hasName = false;


    private JsonScopes() {
    }

    public static JsonScopes root() {
        return new JsonScopes();
    }


    public void push(JsonScope scope) {
        if (scope == ROOT) throw new IllegalArgumentException("Root scope cannot be pushed");

        scopes.push(currentScope);
        currentScope = scope;

        hasEntry = false;
        hasName = false;
    }

    public boolean pop(JsonScope scope) {
        if (scope == ROOT) throw new IllegalArgumentException("Root scope cannot be popped");
        if (currentScope != scope) return false;

        currentScope = scopes.pollFirst();

        hasEntry = true;
        hasName = false;
        return true;
    }


    public ValueStatus beforeWriteValue() {
        return switch (currentScope) {
            case ROOT -> {
                if (hasEntry) yield ValueStatus.NeedsRootSeparator;
                hasEntry = true;
                yield ValueStatus.Ready;
            }
            case OBJECT -> {
                if (!hasName) yield ValueStatus.ExpectedName;
                hasName = false;
                hasEntry = true;
                yield ValueStatus.NeedsNameSeparator;
            }
            case ARRAY -> {
                if (hasEntry) yield ValueStatus.NeedsValueSeparator;
                hasEntry = true;
                yield ValueStatus.Ready;
            }
        };
    }

    public NameStatus beforeWriteName() {
        if (currentScope != OBJECT || hasName) return NameStatus.ExpectedValue;
        hasName = true;
        return hasEntry ? NameStatus.ReadyAfterValueSeparator : NameStatus.Ready;
    }


    public enum ValueStatus {
        Ready,
        NeedsRootSeparator,
        NeedsValueSeparator,
        NeedsNameSeparator,
        ExpectedName
    }

    public enum NameStatus {
        Ready,
        ReadyAfterValueSeparator,
        ExpectedValue
    }
}
