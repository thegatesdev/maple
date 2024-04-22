package com.github.thegatesdev.maple;

import com.github.thegatesdev.maple.io.json.JsonTokener;

import java.io.IOException;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) throws IOException {
        JsonTokener tokener = new JsonTokener(new StringReader("""
                {
                    "hello": "world",
                    "test": false,
                    "number": 1234,
                    "oy": [
                        "one",
                        "two"
                    ]
                }
                """));
        int next;
        do {
            next = tokener.nextTokenId();
            String print = switch (next) {
                default -> "Unknown: " + next;
                case 1 -> "Begin object";
                case 2 -> "Begin array";
                case 3 -> "End object";
                case 4 -> "End array";
                case 5 -> "Name separator";
                case 6 -> "Value separator";
                case 7 -> {
                    tokener.finishLiteralValue();
                    yield "True";
                }
                case 8 -> {
                    tokener.finishLiteralValue();
                    yield "False";
                }
                case 9 -> "Null";
                case 10 -> "Number";
                case 11 -> "String: " + tokener.finishStringValue('"');
            };
            System.out.println(print);
        } while (next > 0);
    }
}
