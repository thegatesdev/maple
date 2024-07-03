package com.github.thegatesdev.maple.io.output;

import com.github.thegatesdev.maple.annotation.internal.*;
import com.github.thegatesdev.maple.exception.*;
import com.github.thegatesdev.maple.io.*;

import java.io.*;
import java.util.*;

@ValueClassCandidate
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

/*
Copyright 2024 Timar Karels

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
