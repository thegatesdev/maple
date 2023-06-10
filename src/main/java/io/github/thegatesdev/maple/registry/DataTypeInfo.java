package io.github.thegatesdev.maple.registry;

import io.github.thegatesdev.maple.read.DataType;
import io.github.thegatesdev.maple.read.ReadableOptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DataTypeInfo {

    private final DataType<?> dataType;
    private String description, stringRep, origin;
    private List<String> possibleValues;
    private ReadableOptions readableOptions;

    public DataTypeInfo(DataType<?> dataType) {
        this.dataType = dataType;
    }

    public DataType<?> dataType() {
        return dataType;
    }

    // --

    public DataTypeInfo description(final String description) {
        this.description = description;
        return this;
    }

    public DataTypeInfo origin(final String origin) {
        this.origin = origin;
        return this;
    }

    public DataTypeInfo representation(final String stringRep) {
        this.stringRep = stringRep;
        return this;
    }

    public DataTypeInfo possibleValues(String... possibleValues) {
        if (this.possibleValues == null) this.possibleValues = new ArrayList<>(possibleValues.length);
        Collections.addAll(this.possibleValues, possibleValues);
        return this;
    }

    public DataTypeInfo possibleValues(Collection<String> possibleValues) {
        if (this.possibleValues == null) this.possibleValues = new ArrayList<>(possibleValues.size());
        this.possibleValues.addAll(possibleValues);
        return this;
    }

    public DataTypeInfo readableOptions(ReadableOptions readableOptions) {
        this.readableOptions = readableOptions;
        return this;
    }


    public ReadableOptions readableOptions() {
        return readableOptions;
    }

    public String description() {
        return description;
    }

    public String origin() {
        return origin;
    }

    public String representation() {
        return stringRep;
    }

    public List<String> possibleValues() {
        return possibleValues == null ? null : Collections.unmodifiableList(possibleValues);
    }
}
