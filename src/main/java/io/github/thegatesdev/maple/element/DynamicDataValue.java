/*
Copyright 2023 Timar Karels

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package io.github.thegatesdev.maple.element;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * An element supplying values of type {@code Type}.
 */
public final class DynamicDataValue<Type> implements DataValue<Type> {

    private final Class<Type> valueType;
    private final Supplier<Type> valueSupplier;

    /**
     * Construct a new {@code DynamicDataValue}.
     *
     * @param valueType     the type of the supplied values
     * @param valueSupplier the supplier of the values
     */
    public DynamicDataValue(Class<Type> valueType, Supplier<Type> valueSupplier) {
        this.valueType = valueType;
        this.valueSupplier = valueSupplier;
    }

    // Value

    @Override
    public <O> DataValue<O> transform(Class<O> newType, Function<Type, O> function) {
        return new DynamicDataValue<>(newType, () -> function.apply(valueSupplier.get()));
    }

    @Override
    public Class<Type> getValueType() {
        return valueType;
    }

    @Override
    public Type getValue() {
        return valueSupplier.get();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
