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
 * This exception is raised when the input for an adapter is invalid for the specification.
 *
 * @author Timar Karels
 */
public final class AdaptException extends RuntimeException {
    /**
     * Create the exception with the given message.
     *
     * @param msg the message describing the cause of the exception
     */
    public AdaptException(String msg) {
        super(msg);
    }

    /**
     * Create the exception with the given message and cause.
     *
     * @param msg   the message describing where the problem happened
     * @param cause the throwable that caused this exception to be raised
     */
    public AdaptException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Create the exception with the given cause.
     * Consider using {@link #AdaptException(String, Throwable)} instead.
     *
     * @param cause the throwable that caused this exception to be raised
     */
    public AdaptException(Throwable cause) {
        super(cause);
    }
}
