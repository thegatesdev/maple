package io.github.thegatesdev.maple.read;

import io.github.thegatesdev.maple.read.struct.DataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/*
Copyright (C) 2022  Timar Karels

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

public class DataTypeInfo {

    private final DataType dataType;
    private String description, stringRep, origin;
    private List<String> possibleValues;
    private ReadableOptions readableOptions;

    public DataTypeInfo(DataType dataType) {
        this.dataType = dataType;
    }

    public DataType dataType() {
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
