package io.github.thegatesdev.maple.read.struct;

import io.github.thegatesdev.maple.data.DataElement;
import io.github.thegatesdev.maple.data.DataList;
import io.github.thegatesdev.maple.data.Keyed;
import io.github.thegatesdev.maple.exception.ElementException;
import io.github.thegatesdev.maple.read.Readable;

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

/**
 * A DataType reads an element to another element of type {@code <E>}, potentially throwing an ElementException.
 */
public interface DataType<E extends DataElement> extends DataTypeHolder<E>, Keyed {

    /**
     * Reads the value from the element.
     * The returned value should always be new and unique.
     */
    E read(DataElement element) throws ElementException;

    /**
     * Returns this same dataType.
     */
    @Override
    default DataType<E> dataType() {
        return this;
    }

    /**
     * Returns a Readable that reads a list of elements using this dataType.
     * This method uses {@link Readable#list(DataType)}
     */
    default Readable<DataList> list() {
        return Readable.list(this);
    }
}
