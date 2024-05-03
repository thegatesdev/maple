package com.github.thegatesdev.maple.io;

import java.io.*;

public interface Escapes {

    int escapeLimit();

    void writeEscaped(Output output, char ch) throws IOException;
}
