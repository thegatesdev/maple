package com.github.thegatesdev.maple.io.output;

import com.github.thegatesdev.maple.exception.*;
import com.github.thegatesdev.maple.io.*;

import java.io.*;
import java.util.*;

public final class WriterOutput implements Output {

    private final Writer writer;

    private WriterOutput(Writer writer) {
        this.writer = writer;
    }


    public static Output create(Writer writer) {
        Objects.requireNonNull(writer, "given writer is null");

        return new WriterOutput(writer);
    }


    @Override
    public void raw(int character) {
        try {
            writer.write(character);
        } catch (IOException e) {
            throw new OutputException(e);
        }
    }

    @Override
    public void raw(String string) {
        try {
            writer.write(string);
        } catch (IOException e) {
            throw new OutputException(e);
        }
    }

    @Override
    public void raw(char[] buffer, int offset, int lenght) {
        try {
            writer.write(buffer, offset, lenght);
        } catch (IOException e) {
            throw new OutputException(e);
        }
    }
}
