package io.github.thegatesdev.maple.maple.exception;

/**
 * This exception is raised when a key is used to access a value, but the key is not present.
 * <p>
 * Do not catch this exception to check for missing entries.
 * Instead, use the dedicated method returning an optional value.
 *
 * @author Timar Karels
 */
public final class ElementKeyNotPresentException extends RuntimeException {

    private static final String MESSAGE = "This key is not present; %s";
    private final String accessedKey;


    /**
     * Create the exception with the given key.
     *
     * @param accessedKey the accessed key
     */
    public ElementKeyNotPresentException(String accessedKey) {
        super(MESSAGE.formatted(accessedKey));
        this.accessedKey = accessedKey;
    }

    /**
     * Get the key that was accessed for this exception to be raised.
     *
     * @return the accessed key
     */
    public String accessedKey() {
        return accessedKey;
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
