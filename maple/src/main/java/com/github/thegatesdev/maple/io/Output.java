package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.exception.*;

import java.math.*;

public interface Output {

    void write(int c) throws OutputException;

    void write(char[] buf, int off, int len) throws OutputException;

    void write(String s) throws OutputException;

    default void write(char[] buf) throws OutputException {
        write(buf, 0, buf.length);
    }


    void writeEscaped(char[] buf, int off, int len, int[] escapes) throws OutputException;

    void writeEscaped(String s, int[] escapes) throws OutputException;


    default void writeValue(boolean b) throws OutputException {
        write(Boolean.toString(b));
    }

    default void writeValue(int i) throws OutputException {
        write(Integer.toString(i));
    }

    default void writeValue(long l) throws OutputException {
        write(Long.toString(l));
    }

    default void writeValue(short s) throws OutputException {
        write(Short.toString(s));
    }

    default void writeValue(byte b) throws OutputException {
        write(Byte.toString(b));
    }

    default void writeValue(float f) throws OutputException {
        write(Float.toString(f));
    }

    default void writeValue(double d) throws OutputException {
        write(Double.toString(d));
    }

    default void writeValue(BigInteger bi) throws OutputException {
        write(bi.toString(10));
    }

    default void writeValue(BigDecimal bd) throws OutputException {
        write(bd.toString());
    }
}
