package io.github.thegatesdev.maple.registry;

import io.github.thegatesdev.maple.data.DataValue;
import io.github.thegatesdev.maple.read.struct.DataType;
import io.github.thegatesdev.maple.read.struct.ReadableOptionsHolder;
import io.github.thegatesdev.maple.registry.struct.Factory;
import io.github.thegatesdev.maple.registry.struct.Identifiable;

import java.util.Collection;

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
public abstract class FactoryRegistry<Data, Fac extends Factory<? extends Data> & ReadableOptionsHolder> implements Identifiable, DataType<DataValue<Data>> {
    protected final String id;
    protected Info info;

    protected FactoryRegistry(String id) {
        this.id = id;
    }

    public abstract Collection<String> keys();

    public abstract Fac get(String key);

    @Override
    public String id() {
        return id;
    }

    @Override
    public Info info() {
        if (info == null) info = new Info(id);
        return info;
    }
}
