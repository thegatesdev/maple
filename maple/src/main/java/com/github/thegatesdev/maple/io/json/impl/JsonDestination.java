package com.github.thegatesdev.maple.io.json.impl;

import com.github.thegatesdev.maple.annotation.internal.*;
import com.github.thegatesdev.maple.io.*;
import com.github.thegatesdev.maple.io.json.util.*;

import java.math.*;
import java.util.*;

@ValueClassCandidate
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
        Objects.requireNonNull(output, "given output is null");
        return new JsonDestination(output, new JsonWriteContext(), JsonScopes.root());
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
            case ExpectedName -> throw new IllegalStateException("Expected name before value");
        }
    }

    private void verifyWriteName() {
        switch (jsonScopes.beforeWriteName()) {
            case ReadyAfterValueSeparator -> output.raw(',');
            case ExpectedValue -> throw new IllegalStateException("Expected value before name");
        }
    }

    private void verifyCloseScope(JsonScope scope) {
        if (!jsonScopes.pop(scope)) throw new IllegalStateException("Not in " + scope.name() + " scope");
    }


    @Override
    public void openObject() {
        verifyWriteValue();
        jsonScopes.push(JsonScope.OBJECT);
        output.raw('{');
    }

    @Override
    public void closeObject() {
        verifyCloseScope(JsonScope.OBJECT);
        output.raw('}');
    }

    @Override
    public void openArray() {
        verifyWriteValue();
        jsonScopes.push(JsonScope.ARRAY);
        output.raw('[');
    }

    @Override
    public void closeArray() {
        verifyCloseScope(JsonScope.ARRAY);
        output.raw(']');
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
