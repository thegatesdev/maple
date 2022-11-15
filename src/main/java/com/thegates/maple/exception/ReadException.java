package com.thegates.maple.exception;

import com.thegates.maple.data.DataElement;

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

public class ReadException extends RuntimeException {

    private final DataElement element;

    public ReadException(DataElement data, String message) {
        super("Error while reading data at %s, %s.".formatted(data.path(), message));
        this.element = data;
    }

    public ReadException(DataElement data, String message, Throwable cause) {
        super("Error while reading data at %s, %s.".formatted(data.path(), message), cause);
        this.element = data;
    }

    public static ReadException requireField(DataElement data, String field) {
        return new ReadException(data, "missing required field '" + field + "'");
    }

    public static ReadException requireType(DataElement data, String typeName) {
        return new ReadException(data, "should be of " + typeName);
    }

    public static ReadException requireType(DataElement data, Class<?> type) {
        return requireType(data, type.getSimpleName());
    }

    public DataElement getElement() {
        return element;
    }
}
