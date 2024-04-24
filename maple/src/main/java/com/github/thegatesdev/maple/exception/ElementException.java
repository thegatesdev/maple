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

package com.github.thegatesdev.maple.exception;

/**
 * This is a generic exception raised when an issue occurs while operating on an element.
 *
 * @author Timar Karels
 */
public sealed class ElementException extends RuntimeException permits ElementKeyNotPresentException,
    ElementTypeException {

    /**
     * Create the exception with the given message.
     *
     * @param msg the message describing the cause of the exception
     */
    public ElementException(String msg) {
        super(msg);
    }

    /**
     * Create the exception with the given message and cause.
     *
     * @param msg   the message describing where the problem happened
     * @param cause the throwable that caused this exception to be raised
     */
    public ElementException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create the exception with the given cause.
     * Consider using {@link #ElementException(String, Throwable)} instead.
     *
     * @param cause the throwable that caused this exception to be raised
     */
    public ElementException(Throwable cause) {
        super(cause);
    }
}
