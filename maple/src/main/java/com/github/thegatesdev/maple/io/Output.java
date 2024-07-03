package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.output.*;

import java.io.*;

public interface Output {


    static Output writer(Writer writer) {
        return WriterOutput.create(writer);
    }


    void raw(int character);

    void raw(String string);

    void raw(char[] buffer, int offset, int lenght);
}
