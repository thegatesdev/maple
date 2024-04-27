package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.format.json.internal.*;

public sealed interface Deserializer permits JsonDeserializer {

    void next();

    void skip();

    Token current();

    byte currentID();


    String readName();

    String readValue();

    String readString();

    boolean readBool();

    void readNull();
}
