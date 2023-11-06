/*
Copyright 2023 Timar Karels

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

package io.github.thegatesdev.maple.exception;

/**
 * Thrown to indicate that an element value did not match the expected type.
 */
public class ValueTypeException extends IllegalArgumentException {

    private static final String MESSAGE = "Invalid value type, expected '%s', got '%s'";

    private final Class<?> expectedType, actualType;

    /**
     * @param expectedType the type of the value that was expected in this location
     * @param actualType   the actual invalid type of the value that was in this location
     */
    public ValueTypeException(Class<?> expectedType, Class<?> actualType) {
        super(MESSAGE.formatted(expectedType.getSimpleName(), actualType.getSimpleName()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    /**
     * @return the type of the value that was expected in this location
     */
    public Class<?> expectedType() {
        return expectedType;
    }

    /**
     * @return the actual invalid type of the value that was in this location
     */
    public Class<?> actualType() {
        return actualType;
    }
}
