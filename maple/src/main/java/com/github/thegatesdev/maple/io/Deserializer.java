package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.io.format.json.internal.*;

public sealed interface Deserializer permits JsonDeserializer {

    void next();

    Token current();

    byte currentID();


    void skip();


    String readName();


    String readValue();

    String readString();

    boolean readBool();

    void readNull();
}
