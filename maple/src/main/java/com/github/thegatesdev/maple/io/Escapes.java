package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.impl.internal.*;

import java.io.*;

public interface Escapes {


    static Escapes json() {
        return JsonEscapes.create();
    }


    boolean couldEscape(char ch);

    boolean writeEscaped(Output output, char ch) throws IOException;
}
