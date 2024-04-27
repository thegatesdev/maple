package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.format.json.internal.*;

import java.math.*;

public sealed interface Serializer permits JsonSerializer {

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
}
