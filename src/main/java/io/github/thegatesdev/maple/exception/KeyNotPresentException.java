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

public class KeyNotPresentException extends RuntimeException {

    private static final String MESSAGE = "Key '%s' is not present";
    private final String key;

    public KeyNotPresentException(String key) {
        super(MESSAGE.formatted(key));
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
