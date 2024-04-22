package com.github.thegatesdev.maple.io.json;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;

import static com.github.thegatesdev.maple.io.internal.TokenId.*;

public final class JsonTokener {

    // Input
    private final Reader reader;

    // Tracking
    private int lineNumber = 0;
    private int columnNumber = 0;

    // State
    private byte currentTokenId = ID_NOT_PRESENT;
    private final StringBuilder buildingValue = new StringBuilder();


    public JsonTokener(Reader reader) {
        this.reader = Objects.requireNonNull(reader, "given reader is null");
    }


    @SuppressWarnings("UnusedReturnValue") // Should just throw on invalid literal
    public boolean finishLiteralValue() throws IOException {
        return switch (currentTokenId) {
            case ID_VALUE_FALSE -> validateNext("alse");
            case ID_VALUE_TRUE -> validateNext("rue");
            case ID_VALUE_NULL -> validateNext("ull");
            default -> throw new IllegalArgumentException("Not a literal"); // TODO Exceptions
        };
    }

    public String finishStringValue(char quoteChar) throws IOException {
        StringBuilder builder = this.buildingValue;
        builder.setLength(0);

        for (; ; ) {
            int next = nextCharacter();

            if (next == -1) throw new IllegalStateException("Unterminated string value");
            if (next == quoteChar) break;

            if (next == '\\') {
                next = nextCharacter();
                builder.append((char) switch (next) {
                    default -> next; // Any character can be escaped, insert literal character
                    case 'b' -> '\b';
                    case 'f' -> '\f';
                    case 'n' -> '\n';
                    case 'r' -> '\r';
                    case 't' -> '\t';
                    case 's' -> ' ';
                    case 'u' -> throw new UnsupportedOperationException("No unicode code point translation");
                });
                continue;
            }

            builder.append((char) next);
        }

        return builder.toString();
    }

    public byte nextTokenId() throws IOException {
        int next = nextRelevantCharacter();
        return currentTokenId = switch (next) {
            case '{' -> ID_BEGIN_OBJECT;
            case '}' -> ID_END_OBJECT;
            case '[' -> ID_BEGIN_ARRAY;
            case ']' -> ID_END_ARRAY;
            case ':' -> ID_NAME_SEPARATOR;
            case ',' -> ID_VALUE_SEPARATOR;
            case '\"' -> ID_VALUE_STRING;
            case '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> ID_VALUE_NUMBER;
            case 't' -> ID_VALUE_TRUE;
            case 'f' -> ID_VALUE_FALSE;
            case 'n' -> ID_VALUE_NULL;
            default -> ID_NOT_AVAILABLE;
        };
    }


    public boolean validateNext(String value) throws IOException {
        for (int i = 0, l = value.length(); i < l; i++) {
            if (nextCharacter() != value.charAt(i)) return false;
        }
        return true;
    }

    public int nextRelevantCharacter() throws IOException {
        int columnNumber = this.columnNumber;
        int lineNumber = this.lineNumber;

        for (; ; ) {
            int next = nextCharacter();
            if (next == -1) return -1;

            columnNumber++;
            switch (next) {
                case '\n':
                    lineNumber++;
                    columnNumber = 0; // Fall through
                case ' ':
                case '\r':
                case '\t':
                    continue;
                default:
                    this.columnNumber = columnNumber;
                    this.lineNumber = lineNumber;
                    return next;
            }
        }
    }

    public int nextCharacter() throws IOException {
        return reader.read();
    }
}
