package io.github.thegatesdev.maple.element;

import io.github.thegatesdev.maple.ElementType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class DataList implements DataElement, DataDictionary<Integer> {

    private final List<DataElement> elementList;

    public DataList(List<DataElement> elementList) {
        this.elementList = elementList;
    }

    public DataList() {
        this(new ArrayList<>());
    }

    // Operations

    public DataElement getOrNull(int index) {
        if (index < 0 || index >= elementList.size()) return null;
        return elementList.get(index);
    }

    public void set(int index, DataElement element) {
        elementList.set(index, element);
    }

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

    // Value

    @Override
    public boolean isEmpty() {
        return elementList.isEmpty();
    }

    @Override
    public List<DataElement> getValue() {
        return elementList;
    }

    // Type

    @Override
    public ElementType getType() {
        return ElementType.LIST;
    }
}
