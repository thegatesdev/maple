package io.github.thegatesdev.maple.maple.io;

import java.io.*;

/**
 * Represents a source of structured data.
 *
 * @author Timar Karels
 */
public interface Source {

    /**
     * Get a data source that reads JSON data from the given Reader input.
     *
     * @param reader the reader to read from
     * @return the new JSON source
     */
    static Source json(Reader reader) {
        throw new UnsupportedOperationException("Not implemented");
    }


    /**
     * Write this source to the given destination.
     *
     * @param handler the destination to write to
     */
    void writeTo(Destination handler);
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
