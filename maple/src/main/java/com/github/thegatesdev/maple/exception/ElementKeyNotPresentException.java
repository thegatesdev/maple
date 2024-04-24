package com.github.thegatesdev.maple.exception;

/**
 * This exception is raised when a key is used to access a value, but the key is not present.
 *
 * @author Timar Karels
 */
public final class ElementKeyNotPresentException extends ElementException {

    private static final String MESSAGE = "This key is not present; %s";
    private final String key;

    /**
     * Create the exception with the given key.
     *
     * @param key the accessed key
     */
    public ElementKeyNotPresentException(String key) {
        super(MESSAGE.formatted(key));
        this.key = key;
    }

    /**
     * Get the key that was accessed for this exception to be raised.
     *
     * @return the accessed key
     */
    public String key() {
        return key;
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
