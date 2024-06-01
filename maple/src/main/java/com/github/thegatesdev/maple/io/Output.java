package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.impl.internal.*;

import java.io.*;
import java.math.*;

public interface Output {


    /**
     * Get an output that uses a writer.
     *
     * @param writer the writer to use
     * @return a writer output with the given parameters
     */
    static Output writer(Writer writer) {
        return WriterOutput.from(writer);
    }


    void raw(int character) throws IOException;

    void raw(String value) throws IOException;

    void escaped(String value, Escapes escapes) throws IOException;


    void value(boolean value) throws IOException;

    void value(short value) throws IOException;

    void value(int value) throws IOException;

    void value(long value) throws IOException;

    void value(float value) throws IOException;

    void value(double value) throws IOException;

    void value(BigInteger value) throws IOException;

    void value(BigDecimal value) throws IOException;

    void nullValue() throws IOException;
}
