package io.github.thegatesdev.maple.read.struct;

import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.data.DataList;
import io.github.thegatesdev.maple.data.Keyed;
import io.github.thegatesdev.maple.read.Readable;

import java.util.*;
import java.util.function.Consumer;

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

public interface DataType<E> extends DataTypeHolder<E>, Keyed {

    /**
     * Returns an element holding the value read from the specified element.
     */
    E read(DataElement element);

    @Override
    default DataType<E> dataType() {
        return this;
    }

    default Readable<DataList> list() {
        return Readable.list(this);
    }

    // -- INFO

    default DataType<E> info(Consumer<Info> consumer) {
        consumer.accept(info());
        return this;
    }

    Info info();

    class Info {
        private static final Map<String, Info> mapped = new HashMap<>();

        public static Info of(String dataTypeId) {
            return mapped.get(dataTypeId);
        }

        public static Set<String> keys() {
            return mapped.keySet();
        }

        private String description, stringRep, origin;
        private String[] possibleValues;

        public Info(String dataTypeId) {
            mapped.putIfAbsent(dataTypeId, this);
        }

        public Info description(final String description) {
            this.description = description;
            return this;
        }

        public Info origin(final String origin) {
            this.origin = origin;
            return this;
        }

        public Info representation(final String stringRep) {
            this.stringRep = stringRep;
            return this;
        }

        public Info possibleValues(String... possibleValues) {
            this.possibleValues = possibleValues;
            return this;
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

        public String[] possibleValues() {
            return possibleValues;
        }

        @Override
        public int hashCode() {
            return Objects.hash(description, stringRep, origin, Arrays.hashCode(possibleValues));
        }
    }
}
