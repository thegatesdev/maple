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

import io.github.thegatesdev.maple.ElementType;
import io.github.thegatesdev.maple.exception.ElementTypeException;

import java.util.function.Consumer;

/**
 * An interface defining the base functionality of an element in a Maple structure.
 */
public sealed interface DataElement permits DataList, DataMap, DataValue {

    // Operations

    /**
     * Process all the descendants of this element.
     * The children of an element will be processed before their parent.
     *
     * @param crawler the crawler to use
     * @return the element with the processed values
     */
    DataElement crawl(Crawler crawler);

    // Value

    /**
     * @return {@code true} if the element does not contain any value
     */
    boolean isEmpty();

    /**
     * @return {@code true} if the element contains any value
     */
    default boolean isPresent() {
        return !isEmpty();
    }

    // Type

    /**
     * @return {@code true} if this element is a map element
     */
    default boolean isMap() {
        return false;
    }

    /**
     * @return {@code true} if this element is a list element
     */
    default boolean isList() {
        return false;
    }

    /**
     * @return {@code true} if this element is a value element
     */
    default boolean isValue() {
        return false;
    }


    /**
     * @return the corresponding element type for this element
     */
    ElementType getType();


    /**
     * @return this element, as a map element
     * @throws ElementTypeException if this element is not a map
     */
    default DataMap asMap() throws ElementTypeException {
        throw new ElementTypeException(ElementType.MAP, getType());
    }

    /**
     * @return this element, as a list element
     * @throws ElementTypeException if this element is not a list
     */
    default DataList asList() throws ElementTypeException {
        throw new ElementTypeException(ElementType.LIST, getType());
    }

    /**
     * @return this element, as a value element
     * @throws ElementTypeException if this element is not a value
     */
    default DataValue<?> asValue() throws ElementTypeException {
        throw new ElementTypeException(ElementType.VALUE, getType());
    }


    /**
     * Run the given action if this element is a map.
     * Run the given else-action if this element is not a map.
     */
    default void ifMap(Consumer<DataMap> action, Runnable elseAction) {
        if (isMap()) action.accept(asMap());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Run the given action if this element is a map.
     */
    default void ifMap(Consumer<DataMap> action) {
        ifMap(action, null);
    }

    /**
     * Run the given action if this element is a list.
     * Run the given else-action if this element is not a list.
     */
    default void ifList(Consumer<DataList> action, Runnable elseAction) {
        if (isList()) action.accept(asList());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Run the given action if this element is a list.
     */
    default void ifList(Consumer<DataList> action) {
        ifList(action, null);
    }

    /**
     * Run the given action if this element is a value.
     * Run the given else-action if this element is not a value.
     */
    default void ifValue(Consumer<DataValue<?>> action, Runnable elseAction) {
        if (isValue()) action.accept(asValue());
        else if (elseAction != null) elseAction.run();
    }

    /**
     * Run the given action if this element is a value.
     */
    default void ifValue(Consumer<DataValue<?>> action) {
        ifValue(action, null);
    }
}
