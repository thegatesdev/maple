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

package com.github.thegatesdev.maple.adapt;

import com.github.thegatesdev.maple.element.Element;
import com.github.thegatesdev.maple.exception.AdaptException;

/**
 * An adapter tries to convert output from a specific parser to a Maple structure.
 *
 * @author Timar Karels
 */
public interface Adapter {
    /**
     * Adapt the given Object value to an element,
     * according to the output type specification.
     *
     * @param input the value to adapt
     * @return the element representing the value
     * @throws AdaptException if the value type does not match any type defined in the specification of the output type
     */
    Element adapt(Object input);
}
