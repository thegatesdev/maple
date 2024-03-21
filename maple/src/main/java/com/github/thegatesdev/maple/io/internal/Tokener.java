package com.github.thegatesdev.maple.io.internal;

import java.io.Reader;

public final class Tokener {

    private final Reader reader;
    private int peeked = -1;

    public Tokener(Reader reader) {
        this.reader = reader;
    }


    /*
    TODO
    The job of the tokener is to provide methods to read the next 'language token'
    E.g. for JSON, braces, strings, quotes etc.
    Peeking is used to view a token without removing it from the input stream.
    This may be necessary for checking tokens before invoking some method depending on the token.

    GSON buffers a lot internally, they may have a good performing parser,
    but it's not really programmer friendly.

    Plan to split this into multiple classes or static methods
    to better visualize what's happening.

    May want to split this into multiple tokener implementations
    for different languages, a generic tokener for e.g. YAML and JSON may
    cause problems.
     */
}
