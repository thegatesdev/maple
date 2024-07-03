package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.json.impl.*;

import java.io.*;
import java.math.*;

public interface Destination {


    static Destination json(Output output) {
        return JsonDestination.create(output);
    }

    static Destination json(Writer writer) {
        return json(Output.writer(writer));
    }


    void openObject();

    void closeObject();

    void openArray();

    void closeArray();


    void name(String name);

    void value(String value);

    void value(boolean value);

    void value(int value);

    void value(long value);

    void value(float value);

    void value(double value);

    void value(BigInteger value);

    void value(BigDecimal value);

    void valueNull();

    default void value(Source source) {
        source.writeTo(this);
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
