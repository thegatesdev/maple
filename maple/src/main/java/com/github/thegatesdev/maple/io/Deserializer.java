package com.github.thegatesdev.maple.io;

public interface Deserializer {

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
