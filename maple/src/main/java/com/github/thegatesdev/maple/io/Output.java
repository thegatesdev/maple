package com.github.thegatesdev.maple.io;

import java.math.*;

public interface Output {

    void write(int c);

    void write(String s);

    void writeEscaped(String s, int[] escapes);


    default void writeValue(boolean b) {
        write(Boolean.toString(b));
    }

    default void writeValue(int i) {
        write(Integer.toString(i));
    }

    default void writeValue(long l) {
        write(Long.toString(l));
    }

    default void writeValue(short s) {
        write(Short.toString(s));
    }

    default void writeValue(byte b) {
        write(Byte.toString(b));
    }

    default void writeValue(float f) {
        write(Float.toString(f));
    }

    default void writeValue(double d) {
        write(Double.toString(d));
    }

    default void writeValue(BigInteger bi) {
        write(bi.toString(10));
    }

    default void writeValue(BigDecimal bd) {
        write(bd.toString());
    }
}
