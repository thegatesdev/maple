package com.github.thegatesdev.maple.io;

import java.math.*;

public interface Serializer {

    void writeValue(String value);

    void writeValue(boolean value);

    void writeValue(int value);

    void writeValue(long value);

    void writeValue(float value);

    void writeValue(double value);

    void writeValue(BigInteger value);

    void writeValue(BigDecimal value);


    void openObject();

    void writeKey(String key);

    void closeObject();


    void openArray();

    void closeArray();
}
