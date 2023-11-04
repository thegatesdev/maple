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

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * An element holding a list of elements.
 */
public final class DataList implements DataElement, DataDictionary<Integer> {

    private final DataElement[] elements;

    private DataList(DataElement[] elements) {
        this.elements = elements;
    }


    /**
     * Create a new list element builder.
     *
     * @return the new builder
     */
    public static Builder builder(){
        return new Builder();
    }

    /**
     * Create a new list element builder with the given initial capacity.
     *
     * @param initialCapacity the initial capacity of the builder
     * @return the new builder
     */
    public static Builder builder(int initialCapacity){
        return new Builder(initialCapacity);
    }

    // Operations

    @Override
    public DataElement find(Integer index) {
        return find((int)index);
    }

    /**
     * @see DataList#find(Integer)
     */
    public DataElement find(int index){
        if (index < 0 || index >= elements.length) return null;
        return elements[index];
    }

    @Override
    public void each(Consumer<DataElement> elementConsumer) {
        for (final var element : elements)
            elementConsumer.accept(element);
    }

    @Override
    public DataElement crawl(Function<DataElement, DataElement> crawlFunction) {
        var output = new DataElement[elements.length];
        for (int i = 0; i < elements.length; i++)
            output[i] = crawlFunction.apply(elements[i].crawl(crawlFunction));
        return new DataList(output);
    }

    @Override
    public DataList transform(Function<DataElement, DataElement> transformFunction) {
        var output = new DataElement[elements.length];
        for (int i = 0; i < elements.length; i++)
            output[i] = transformFunction.apply(elements[i]);
        return new DataList(output);
    }

    // Information

    @Override
    public int size() {
        return elements.length;
    }

    // Value

    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }

    // Type

    @Override
    public ElementType type() {
        return ElementType.LIST;
    }

    @Override
    public boolean isList() {
        return true;
    }

    @Override
    public DataList asList() {
        return this;
    }


    /**
     * A builder for {@link DataList}.
     */
    public static class Builder{
        private final List<DataElement> buildingElements;


        private Builder(){
            this(5);
        }

        private Builder(int initialCapacity){
            buildingElements = new ArrayList<>(initialCapacity);
        }


        /**
         * Build the elements in the builder to a new list element.
         *
         * @return the new list element
         */
        public DataList build(){
            return new DataList(buildingElements.toArray(new DataElement[0]));
        }


        /**
         * Add an element to the builder.
         *
         * @param element the element to add
         */
        public Builder add(DataElement element){
            buildingElements.add(element);
            return this;
        }

        /**
         * Add all elements from the given collection to the builder.
         *
         * @param elements the elements to add
         */
        public Builder addFrom(Collection<DataElement> elements){
            buildingElements.addAll(elements);
            return this;
        }

        /**
         * Add all elements from the given iterable to the builder.
         *
         * @param elements the elements to add
         */
        public Builder addFrom(Iterable<DataElement> elements){
            elements.forEach(buildingElements::add);
            return this;
        }

        /**
         * Add all elements from the given list element to the builder.
         *
         * @param dataList the list to add the elements from
         */
        public Builder addFrom(DataList dataList){
            buildingElements.addAll(Arrays.asList(dataList.elements));
            return this;
        }
    }
}
