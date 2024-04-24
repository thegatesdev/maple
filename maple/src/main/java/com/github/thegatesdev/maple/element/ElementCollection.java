package com.github.thegatesdev.maple.element;

import java.util.function.*;
import java.util.stream.*;

/**
 * An element containing a collection of child elements.
 *
 * @author Timar Karels
 */
public sealed interface ElementCollection extends Element permits DictElement, ListElement {

    /**
     * Iterate the values in this collection using the given action.
     *
     * @param action the action to run for each value
     * @throws NullPointerException if the given action is null
     */
    void each(Consumer<Element> action);

    /**
     * Iterate the descendants in this collection using the given action.
     * When an element collection is encountered, its values are crawled first.
     *
     * @param action the action to run for each descendant
     * @throws NullPointerException if the given action is null
     */
    void crawl(Consumer<Element> action);


    /**
     * Get a stream of the values in this collection.
     *
     * @return a new stream with the values
     */
    Stream<Element> stream();

    /**
     * Get a list element containing the values in this collection.
     * If this element is already a list element, returns the same element.
     * <br>
     * The resulting list element is cached in a thread safe manner,
     * subsequent calls will return the same object.
     *
     * @return the list element with the values
     */
    ListElement values();


    /**
     * Get the amount of items in this collection.
     *
     * @return the item count
     */
    int count();
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
