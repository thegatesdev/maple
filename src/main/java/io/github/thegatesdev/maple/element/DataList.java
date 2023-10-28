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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * An element holding a list of elements.
 */
public final class DataList implements DataElement, DataDictionary<Integer> {

    private final List<DataElement> elementList;

    private DataList(List<DataElement> elementList) {
        this.elementList = elementList;
    }

    /**
     * Construct a new empty {@code DataList}.
     */
    public DataList() {
        this(new ArrayList<>());
    }

    /**
     * Construct a new empty {@code DataList} with the given initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public DataList(int initialCapacity) {
        this(new ArrayList<>(initialCapacity));
    }

    // Self

    @Override
    public DataList structureCopy() {
        var output = new ArrayList<DataElement>(elementList.size());
        for (int i = 0; i < elementList.size(); i++)
            output.set(i, elementList.get(i).structureCopy());
        return new DataList(output);
    }

    // Operations

    /**
     * @see DataList#getOrNull(Integer)
     */
    public DataElement getOrNull(int index) {
        if (index < 0 || index >= elementList.size()) return null;
        return elementList.get(index);
    }

    /**
     * @see DataList#set(Integer, DataElement)
     */
    public void set(int index, DataElement element) {
        elementList.set(index, element);
    }


    /**
     * Append an element to the list.
     *
     * @param element the element to append
     */
    public void add(DataElement element) {
        elementList.add(element);
    }

    @Override
    public DataElement getOrNull(Integer index) {
        return getOrNull((int) index);
    }

    @Override
    public void set(Integer index, DataElement element) {
        set((int) index, element);
    }

    @Override
    public void each(Consumer<DataElement> elementConsumer) {
        elementList.forEach(elementConsumer);
    }

    @Override
    public int crawl(Crawler crawler) {
        int processed = 0;
        for (int i = 0; i < elementList.size(); i++) {
            var element = elementList.get(i);
            processed += element.crawl(crawler) + 1; // Crawl containing elements

            var result = crawler.process(element); // Crawl element itself
            if (result.isPresent()) elementList.set(i, result.get()); // Replace original
        }
        return processed;
    }

    // Information

    @Override
    public int size() {
        return elementList.size();
    }

    // Value

    @Override
    public boolean isEmpty() {
        return elementList.isEmpty();
    }

    // Type

    @Override
    public ElementType getType() {
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
}
