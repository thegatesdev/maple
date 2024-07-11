package io.github.thegatesdev.maple.io;

import io.github.thegatesdev.maple.io.output.*;

import java.io.*;

/**
 * Represents a way to output raw characters.
 */
public interface Output extends AutoCloseable {


    /**
     * Get an output that writes data to a {@code Writer}.
     *
     * @param writer the write to use
     * @return the new writer destination
     */
    static Output writer(Writer writer) {
        return WriterOutput.create(writer);
    }


    /**
     * Output a single character.
     *
     * @param character the character to output
     */
    void raw(int character);

    /**
     * Output the characters in the given string.
     *
     * @param string the string to output
     */
    void raw(String string);

    /**
     * Outputs a portion of an array of characters.
     *
     * @param buffer the array of characters
     * @param offset the offset from which to start outputting characters
     * @param lenght the number of characters to output
     */
    void raw(char[] buffer, int offset, int lenght);

    @Override
    void close();
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
