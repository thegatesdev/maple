package com.github.thegatesdev.maple.io.internal;

import com.github.thegatesdev.maple.io.*;

import java.io.*;

public record WriterOutput(Writer writer) implements Output {
    @Override
    public void write(int c) {
        try {
            writer.write(c);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(String s) {
        try {
            writer.write(s);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
