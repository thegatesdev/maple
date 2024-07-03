package com.github.thegatesdev.maple.io;

import java.io.*;

public interface Source {

    static Source json(Reader reader) {
        throw new UnsupportedOperationException("Not implemented");
    }


    void writeTo(Destination handler);
}
