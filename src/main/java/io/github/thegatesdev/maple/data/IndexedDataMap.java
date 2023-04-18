package io.github.thegatesdev.maple.data;

import java.util.ArrayList;
import java.util.List;

public class IndexedDataMap extends DataMap implements IndexedElement {

    private List<DataElement> indexed;

    @Override
    public IndexedDataMap put(String key, DataElement element) throws RuntimeException {
        super.put(key, element);
        if (indexed == null) indexed = new ArrayList<>(1);
        if (size() > indexed.size()) indexed.add(element);
        else indexed.set(size()-1,element);
        return this;
    }

    @Override
    public DataElement get(int index) {
        final var el = getOrNull(index);
        return el == null ? new DataNull().setData(this, "[" + index + "]") : el;
    }

    @Override
    public DataElement getOrNull(int index) {
        return index < 0 || index >= indexed.size() ? null : indexed.get(index);
    }
}
