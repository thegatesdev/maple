package com.github.thegatesdev.maple;

import com.github.thegatesdev.maple.io.json.JsonTokener;

import java.io.IOException;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) throws IOException {
        JsonTokener tokener = new JsonTokener(new StringReader("""
                {
                    "hello": "world",
                    "test": "me", // test comment
                    "oy": [
                     /* Today is a great day
                     Because this works! */
                        "one",
                        "two"
                    ] # Goober
                } // Hi
                """));
        int next = tokener.nextRelevantCharacter();
        while (next > -1) {
            System.out.print((char) next);
            next = tokener.nextRelevantCharacter();
        }
        System.out.println();
    }
}
