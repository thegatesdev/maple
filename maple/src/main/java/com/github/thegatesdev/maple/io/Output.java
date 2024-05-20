package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.impl.internal.*;

import java.io.*;
import java.math.*;

public interface Output {


    /**
     * Get an output using a writer
     *
     * @param writer the writer to use
     * @return a writer output with the given parameters
     */
    static Output writer(Writer writer) {
        return WriterOutput.from(writer);
    }


    void raw(int ch) throws IOException;

    void raw(char[] buf, int offset, int len) throws IOException;

    void raw(String string) throws IOException;


    void escaped(char[] buf, int offset, int len, Escapes escapes) throws IOException;

    void escaped(String s, Escapes escapes) throws IOException;


    void value(boolean b) throws IOException;

    void value(short i) throws IOException;

    void value(int i) throws IOException;

    void value(long l) throws IOException;

    void value(float f) throws IOException;

    void value(double d) throws IOException;

    void value(BigInteger bI) throws IOException;

    void value(BigDecimal bD) throws IOException;

    void nullValue() throws IOException;
}
