package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.registry.DataTypeInfo;
import io.github.thegatesdev.maple.registry.Identifiable;

import java.util.function.Consumer;

public interface DataType<Value> extends DataTypeHolder<Value>, Identifiable {

    DataElement read(DataElement element);

    @Override
    default DataType<Value> dataType() {
        return this;
    }


    // -- INFO

    default DataType<Value> info(Consumer<DataTypeInfo> consumer) {
        consumer.accept(info());
        return this;
    }

    DataTypeInfo info();
}
