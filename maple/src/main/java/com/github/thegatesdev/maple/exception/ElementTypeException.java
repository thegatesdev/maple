package com.github.thegatesdev.maple.exception;

import com.github.thegatesdev.maple.element.*;

/**
 * This exception is raised when a method expects an element to be of some type, but it is of some other type.
 *
 * @author Timar Karels
 */
public final class ElementTypeException extends RuntimeException {

    private static final String MESSAGE = "Invalid element type; expected %s, got %s";
    private final ElementType expectedType, actualType;

    /**
     * Create the exception with the expected and actual type of the element.
     *
     * @param expectedType the expected type
     * @param actualType   the actual type
     */
    public ElementTypeException(ElementType expectedType, ElementType actualType) {
        super(MESSAGE.formatted(expectedType.name(), actualType.name()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    /**
     * Get the type that was expected from the element.
     *
     * @return the expected type
     */
    public ElementType expectedType() {
        return expectedType;
    }

    /**
     * Get the actual type of the element.
     *
     * @return the actual type
     */
    public ElementType actualType() {
        return actualType;
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
