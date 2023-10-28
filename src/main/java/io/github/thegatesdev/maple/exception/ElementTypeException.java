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

import io.github.thegatesdev.maple.ElementType;

/**
 * Thrown to indicate that an element did not match the expected type.
 */
public class ElementTypeException extends IllegalArgumentException {

    private static final String MESSAGE = "Invalid element type, expected %s, got %s";
    private final ElementType expectedType, actualType;

    /**
     * @param expectedType the type of the element that was expected in this location
     * @param actualType   the actual invalid type that was in this location
     */
    public ElementTypeException(ElementType expectedType, ElementType actualType) {
        super(MESSAGE.formatted(expectedType.getInlineName(), actualType.getInlineName()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    /**
     * @return the type of the element that was expected in this location
     */
    public ElementType getExpectedType() {
        return expectedType;
    }

    /**
     * @return the actual invalid type that was in this location
     */
    public ElementType getActualType() {
        return actualType;
    }
}
