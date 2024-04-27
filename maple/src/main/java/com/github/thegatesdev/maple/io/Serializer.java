package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.format.json.internal.*;

import java.math.*;

public sealed interface Serializer permits JsonSerializer {

    void writeValue(String value);

    void writeValue(boolean value);

    void writeValue(int value);

    void writeValue(long value);

    void writeValue(float value);

    void writeValue(double value);

    void writeValue(BigInteger value);

    void writeValue(BigDecimal value);


    void openObject();

    void writeName(String key);

    void closeObject();


    void openArray();

    void closeArray();
}
