package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.json.impl.*;

import java.io.*;
import java.math.*;

public interface Destination {


    static Destination json(Output output) {
        return JsonDestination.create(output);
    }

    static Destination json(Writer writer) {
        return json(Output.writer(writer));
    }


    void openObject();

    void closeObject();

    void openArray();

    void closeArray();


    void name(String name);

    void value(String value);

    void value(boolean value);

    void value(int value);

    void value(long value);

    void value(float value);

    void value(double value);

    void value(BigInteger value);

    void value(BigDecimal value);

    void valueNull();

    default void value(Source source) {
        source.writeTo(this);
    }
}
