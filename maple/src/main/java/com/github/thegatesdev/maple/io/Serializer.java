package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.exception.*;
import com.github.thegatesdev.maple.io.format.json.internal.*;

import java.math.*;

public sealed interface Serializer permits JsonSerializer {

    void openObject() throws OutputException;

    void closeObject() throws OutputException;

    void openArray() throws OutputException;

    void closeArray() throws OutputException;


    void name(String name) throws OutputException;

    void value(String value) throws OutputException;

    void value(boolean value) throws OutputException;

    void value(int value) throws OutputException;

    void value(long value) throws OutputException;

    void value(float value) throws OutputException;

    void value(double value) throws OutputException;

    void value(BigInteger value) throws OutputException;

    void value(BigDecimal value) throws OutputException;
}
