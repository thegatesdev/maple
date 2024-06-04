package com.github.thegatesdev.maple.io;

import java.io.*;

public interface Escapes {

    boolean shouldEscape(char ch);

    void writeEscaped(Output output, char ch) throws IOException;
}
