package io.github.thegatesdev.maple.exception;

import io.github.thegatesdev.maple.DataElement;

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
 * An exception thrown by an element. Mostly meant for the end user, used in assertions.
 */
public class ElementException extends RuntimeException {

    /**
     * The element where this exception was thrown for.
     */
    private final DataElement element;

    /**
     * Constructs a new ElementException.
     *
     * @param data    The element the error happened at.
     * @param message The message for the exception.
     */
    public ElementException(DataElement data, String message) {
        this(data, message, null);
    }

    /**
     * Constructs a new ElementException.
     *
     * @param cause   The Throwable to take as a cause.
     * @param data    The element the error happened at.
     * @param message The message for the exception.
     */
    public ElementException(DataElement data, String message, Throwable cause) {
        super("Error at %s; %s.".formatted(String.join(".", data.path()), message), cause);
        this.element = data;
    }

    /**
     * Constructs a new ElementException for a missing field.
     *
     * @param data  The element the error happened at.
     * @param field The field that is required.
     * @return A new ElementException.
     */
    public static ElementException requireField(DataElement data, String field) {
        return new ElementException(data, "missing required field '" + field + "'");
    }

    /**
     * Constructs a new ElementException for an element not being the required type.
     *
     * @param data The element the error happened at.
     * @param type The type this element is required to be.
     * @return A new ElementException.
     */
    public static ElementException requireType(DataElement data, Class<?> type) {
        return requireType(data, type.getSimpleName());
    }

    /**
     * Constructs a new ElementException for an element not being the required type.
     *
     * @param data     The element the error happened at.
     * @param typeName The name of the type this element is required to be.
     * @return A new ElementException.
     */
    public static ElementException requireType(DataElement data, String typeName) {
        return new ElementException(data, "should be of " + typeName);
    }

    /**
     * @return The element this ElementException was thrown on.
     */
    public DataElement getElement() {
        return element;
    }
}
