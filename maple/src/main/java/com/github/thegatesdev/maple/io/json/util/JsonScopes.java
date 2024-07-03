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
