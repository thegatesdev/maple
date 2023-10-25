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

public class ValueTypeException extends IllegalArgumentException {

    private static final String MESSAGE = "Invalid value type, expected '%s', got '%s'";

    private final Class<?> expectedType, actualType;

    public ValueTypeException(Class<?> expectedType, Class<?> actualType) {
        super(MESSAGE.formatted(expectedType.getSimpleName(), actualType.getSimpleName()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    public Class<?> getActualType() {
        return actualType;
    }
}
