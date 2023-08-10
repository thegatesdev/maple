package io.github.thegatesdev.maple.read.struct;

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

import io.github.thegatesdev.maple.data.DataElement;

/**
 * Ease of use DataType implementation for storing the key.
 */
public abstract class AbstractDataType<E extends DataElement> implements DataType<E> {
    protected final String key;

    protected AbstractDataType(String key) {
        this.key = key;
    }

    /**
     * Gets the key of this dataType.
     */
    @Override
    public String key() {
        return key;
    }
}
