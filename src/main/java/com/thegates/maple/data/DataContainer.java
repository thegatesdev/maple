package com.thegates.maple.data;

import com.thegates.maple.exception.ReadException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DataContainer extends DataElement {

    protected Object value;

    DataContainer(Object value) {
        setValue(value);
    }

    DataContainer() {
    }

    public static DataContainer of(Object data) {
        DataContainer output = new DataContainer();
        output.setValue(data);
        return output;
    }


    @Override
    public DataContainer setParent(DataElement parent) {
        super.setParent(parent);
        return this;
    }

    @Override
    public DataContainer setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public String getDescription() {
        String base = super.getDescription() + ": ";
        if (value == null) return base + "DataContainer with no value";
        else return String.format(base + "DataContainer with value type %s", value.getClass());
    }

    @Override
    public String toString() {
        return getDescription();
    }


    @SuppressWarnings("unchecked")
    public <T> T getUnsafe() {
        return (T) value;
    }

    public void setValue(Object value) {
        Object outputVal;
        if (value instanceof DataElement element) {
            if (element instanceof DataContainer container) {
                outputVal = container.value;
            } else {
                outputVal = element.setParent(this);
            }
        } else outputVal = value;
        synchronized (this) {
            this.value = outputVal;
        }
    }

    public <T> T getOrThrow(Class<T> clazz) {
        if (isOf(clazz)) return clazz.cast(value);
        else
            throw new ReadException(this, "unexpected value type, expected " + clazz.getSimpleName() + ", got " + value.getClass().getSimpleName());
    }

    public <T> T getOrNull(Class<T> clazz) {
        return isOf(clazz) ? clazz.cast(value) : null;
    }

    public boolean isPresent() {
        return value != null;
    }


    public boolean isOf(Class<?> clazz) {
        return clazz.isInstance(value);
    }

    public boolean isDataList() {
        return value instanceof DataList;
    }

    public DataList getAsDataList() {
        return isDataList() ? (DataList) value : new DataList().setParent(this);
    }

    public boolean isDataMap() {
        return value instanceof DataMap;
    }

    public DataMap getAsDataMap() {
        return isDataMap() ? ((DataMap) value) : new DataMap().setParent(this);
    }


    public boolean isString() {
        return value instanceof String;
    }

    public String getAsString() {
        return isString() ? (String) value : null;
    }

    public String getAsString(String def) {
        return isString() ? (String) value : def;
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public boolean getAsBoolean(boolean def) {
        return isBoolean() ? (boolean) value : def;
    }

    public int getAsInt(int def) {
        return isNumber() ? ((Number) value).intValue() : def;
    }

    public double getAsDouble(double def) {
        return isNumber() ? ((Number) value).doubleValue() : def;
    }

    public float getAsFloat(float def) {
        return isNumber() ? ((Number) value).floatValue() : def;
    }

    public long getAsLong(long def) {
        return isNumber() ? ((Number) value).longValue() : def;
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public boolean isList() {
        return value instanceof List;
    }

    public List<?> getAsList() {
        return isList() ? (List<?>) value : null;
    }

    public List<String> getAsStringList() {
        if (!isDataList()) return Collections.emptyList();
        return getAsDataList().getAsListOf(String.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataContainer container)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(value, container.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }
}
