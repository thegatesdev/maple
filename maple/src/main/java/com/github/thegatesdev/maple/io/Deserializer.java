package com.github.thegatesdev.maple.io;

public interface Deserializer {

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
