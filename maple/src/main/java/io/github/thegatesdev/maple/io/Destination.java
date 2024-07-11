package io.github.thegatesdev.maple.io;

import io.github.thegatesdev.maple.io.json.impl.*;

import java.math.*;

/**
 * Represents a destination of structured data.
 *
 * @author Timar Karels
 */
public interface Destination {


    /**
     * Get a data destination that writes JSON data to the given output.
     *
     * @param output the output to write to
     * @return the new JSON destination
     */
    static Destination json(Output output) {
        return JsonDestination.create(output);
    }


    /**
     * Create and enter a new object scope.
     * All following values will be written in this scope, until it is exited.
     * <br>
     * The object scope requires pairs of a name and a value,
     * otherwise an exception is thrown.
     *
     * @throws IllegalStateException if a name was expected
     */
    void openObject();

    /**
     * Close the current object scope, and enter the previous scope.
     *
     * @throws IllegalStateException if the current scope is not an object scope
     */
    void closeObject();

    /**
     * Create and enter a new array scope.
     * All following values will be written in this scope, until it is exited.
     * <br>
     * The array scope only takes values,
     * writing names will cause an exception.
     *
     * @throws IllegalStateException if a name was expected
     */
    void openArray();

    /**
     * Close the current array scope, and enter the previous scope.
     *
     * @throws IllegalStateException if the current scope is not an array scope
     */
    void closeArray();


    /**
     * Write the name for an object name-value pair.
     *
     * @param name the name to write
     * @throws IllegalStateException if a value was expected
     */
    void name(String name);

    /**
     * Write a value, either for an object name-value pair,
     * or for an array.
     *
     * @param value the value to write
     */
    void value(String value);

    /**
     * Write a value, either for an object name-value pair,
     * or for an array.
     *
     * @param value the value to write
     */
    void value(boolean value);

    /**
     * Write a value, either for an object name-value pair,
     * or for an array.
     *
     * @param value the value to write
     */
    void value(int value);

    /**
     * Write a value, either for an object name-value pair,
     * or for an array.
     *
     * @param value the value to write
     */
    void value(long value);

    /**
     * Write a value, either for an object name-value pair,
     * or for an array.
     *
     * @param value the value to write
     */
    void value(float value);

    /**
     * Write a value, either for an object name-value pair,
     * or for an array.
     *
     * @param value the value to write
     */
    void value(double value);

    /**
     * Write a value, either for an object name-value pair,
     * or for an array.
     *
     * @param value the value to write
     */
    void value(BigInteger value);

    /**
     * Write a value, either for an object name-value pair,
     * or for an array.
     *
     * @param value the value to write
     */
    void value(BigDecimal value);

    /**
     * Write a null value, either for an object name-value pair,
     * or for an array.
     */
    void valueNull();

    /**
     * Write the given source to this destination.
     * <br>
     * Shortcut for {@link Source#writeTo(Destination)}
     *
     * @param source the source to write
     */
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
