package io.github.thegatesdev.maple.io.json.impl;

import io.github.thegatesdev.maple.exception.*;
import io.github.thegatesdev.maple.io.*;
import io.github.thegatesdev.maple.io.json.util.*;

import java.math.*;
import java.util.*;

public class JsonDestination implements Destination {

    private final Output output;
    private final JsonWriteContext context;
    private final JsonScopes jsonScopes;


    private JsonDestination(Output output, JsonWriteContext context, JsonScopes jsonScopes) {
        this.context = context;
        this.output = output;
        this.jsonScopes = jsonScopes;
    }

    public static Destination create(Output output) {
        return create(output, new JsonWriteContext());
    }

    public static Destination create(Output output, JsonWriteContext context) {
        Objects.requireNonNull(output, "given output is null");
        Objects.requireNonNull(context, "given context is null");

        return new JsonDestination(output, context, JsonScopes.root());
    }


    private void writeString(String value) {
        output.raw('"');
        context.writeEscaped(output, value);
        output.raw('"');
    }


    private void verifyWriteValue() {
        switch (jsonScopes.beforeWriteValue()) {
            case NeedsNameSeparator -> output.raw(':');
            case NeedsRootSeparator -> output.raw(' ');
            case NeedsValueSeparator -> output.raw(',');
            case ExpectedName -> throw new InvalidJsonException("Expected name before value");
        }
    }

    private void verifyWriteName() {
        switch (jsonScopes.beforeWriteName()) {
            case ReadyAfterValueSeparator -> output.raw(',');
            case ExpectedValue -> throw new InvalidJsonException("Expected value");
        }
    }

    private void verifyCloseScope(JsonScope scope) {
        if (!jsonScopes.pop(scope)) throw new InvalidJsonException("Not in " + scope.name() + " scope");
    }


    private void openScope(JsonScope scope) {
        verifyWriteValue();
        jsonScopes.push(scope);
        output.raw(scope.openChar());
    }

    private void closeScope(JsonScope scope) {
        verifyCloseScope(scope);
        output.raw(scope.closeChar());
    }


    @Override
    public void openObject() {
        openScope(JsonScope.Object);
    }

    @Override
    public void closeObject() {
        closeScope(JsonScope.Object);
    }

    @Override
    public void openArray() {
        openScope(JsonScope.Array);
    }

    @Override
    public void closeArray() {
        closeScope(JsonScope.Array);
    }


    @Override
    public void name(String name) {
        verifyWriteName();
        writeString(name);
    }


    @Override
    public void value(String value) {
        verifyWriteValue();
        writeString(value);
    }

    @Override
    public void value(boolean value) {
        verifyWriteValue();
        if (value) context.writeLiteralTrue(output);
        else context.writeLiteralFalse(output);
    }

    @Override
    public void value(int value) {
        verifyWriteValue();
        output.raw(Integer.toString(value, 10));
    }

    @Override
    public void value(long value) {
        verifyWriteValue();
        output.raw(Long.toString(value));
    }

    @Override
    public void value(float value) {
        verifyWriteValue();
        output.raw(Float.toString(value));
    }

    @Override
    public void value(double value) {
        verifyWriteValue();
        output.raw(Double.toString(value));
    }

    @Override
    public void value(BigInteger value) {
        verifyWriteValue();
        output.raw(value.toString(10));
    }

    @Override
    public void value(BigDecimal value) {
        verifyWriteValue();
        output.raw(value.toString());
    }

    @Override
    public void valueNull() {
        verifyWriteValue();
        context.writeLiteralNull(output);
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
