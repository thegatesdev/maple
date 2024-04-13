package com.github.thegatesdev.maple.io;

public sealed interface Tokener permits com.github.thegatesdev.maple.io.json.JsonTokener {

    Token next();


    void skipObject();

    void skipArray();

    void skipField();


    String nextName();

    String nextString();

    int nextInt();

    long nextLong();

    double nextDouble();

    boolean nextBoolean();

    void nextNull();
}
