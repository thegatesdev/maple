package io.github.thegatesdev.maple.data;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * An array element backed by an array.
 */
public class DataArray extends DataElement {

    private final DataElement[] value;

    private DataArray(DataElement[] value) {
        this.value = value;
    }

    /**
     * Constructs a DataArray with its data unset.
     *
     * @param size The size to initialize the array with.
     */
    public DataArray(int size) {
        value = new DataElement[size];
    }

    /**
     * Constructs a DataArray with its parent defaulted to {@code null}
     *
     * @param name The name to initialize the data with.
     * @param size The size to initialize the array with.
     */
    public DataArray(final String name, int size) {
        super(name);
        value = new DataElement[size];
    }


    /**
     * Read an array to a DataArray
     *
     * @param data The array to read from.
     * @return A new DataArray with the same size as the array,
     * the values read using {@link DataElement#readOf(Object)}
     */
    public static DataArray read(Object[] data) {
        final DataArray output = new DataArray(data.length);
        for (int i = 0; i < data.length; i++) {
            output.set(i, DataElement.readOf(data[i]));
        }
        return output;
    }

    /**
     * Set an element at the specified index.
     *
     * @param index   The index to put the element at.
     * @param element The element to put.
     * @return This same DataArray.
     */
    public DataArray set(int index, DataElement element) {
        if (element.isDataSet())
            throw new IllegalArgumentException("This element already has a parent / name. Did you mean to copy() first?");
        if (value.length > index) value[index] = element.setData(this, "[" + index + "]");
        return this;
    }

    /**
     * Get the element at this index.
     *
     * @param index The index of the element.
     * @return The element at this index, or a new {@link DataNull}.
     */
    public DataElement get(int index) {
        final DataElement element = getOrNull(index);
        if (element == null) return new DataNull().setData(this, "[" + index + "]");
        return element;
    }

    /**
     * Get the element at this index, or null.
     *
     * @param index The index of the element.
     * @return The element at this index, or {@code null}.
     */
    public DataElement getOrNull(int index) {
        if (value.length <= index) return null;
        return value[index];
    }


    @Override
    public DataElement[] value() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public boolean isEmpty() {
        return value.length == 0;
    }

    @Override
    protected Object raw() {
        return value;
    }

    @Override
    public DataElement clone() {
        return new DataArray(value());
    }

    @Override
    public DataArray asArray() {
        return this;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public void ifArray(final Consumer<DataArray> arrayConsumer, final Runnable elseAction) {
        arrayConsumer.accept(this);
    }
}
