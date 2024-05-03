package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.impl.internal.*;

import java.io.*;
import java.math.*;

public sealed interface Serializer permits JsonSerializer {

    void openObject() throws IOException;

    void closeObject() throws IOException;

    void openArray() throws IOException;

    void closeArray() throws IOException;


    void name(String name) throws IOException;

    void value(String value) throws IOException;

    void value(boolean value) throws IOException;

    void value(int value) throws IOException;

    void value(long value) throws IOException;

    void value(float value) throws IOException;

    void value(double value) throws IOException;

    void value(BigInteger value) throws IOException;

    void value(BigDecimal value) throws IOException;

    void nullValue() throws IOException;
}
