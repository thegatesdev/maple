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

package io.github.thegatesdev.maple.conversion;

import io.github.thegatesdev.maple.element.DataList;
import io.github.thegatesdev.maple.element.DataMap;

import java.util.List;
import java.util.Map;

/**
 * Provides methods to convert {@code Map} and {@code List} instances to their element equivalent.
 */
public interface Conversion {

    /**
     * Convert the given {@code Map} to a new {@code DataMap}.
     *
     * @param someMap the {@code Map} to convert
     * @return a new {@code DataMap} holding the convertible entries
     */
    DataMap convertMap(Map<?, ?> someMap);

    /**
     * Convert the given {@code List} to a new {@code DataList}.
     *
     * @param someList the {@code List} to convert
     * @return a new {@code DataList} holding the convertible values
     */
    DataList convertList(List<?> someList);
}
