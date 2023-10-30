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

package io.github.thegatesdev.maple.element;

import java.util.Optional;

/**
 * Processes the contents of an element, including any descendants.
 */
@FunctionalInterface
public interface Crawler {

    /**
     * Process a found element.
     * Returns the element replacement, or an empty {@code Optional} to keep the original.
     *
     * @param element the element that was found
     * @return the optional replacement
     */
    Optional<DataElement> process(DataElement element);
}
