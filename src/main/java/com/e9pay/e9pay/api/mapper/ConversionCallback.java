package com.e9pay.e9pay.api.mapper;

/**
 * A simple interface type used to perform the conversion between a field's {@link String} representation and a Java-type.
 *
 * @author Vivek Adhikari
 * @param <O>
 *     The type of object that should be converted.
 */
public interface ConversionCallback<O> {

    /**
     * Convert the input value to a type-safe output.
     *
     * @param property
     *     The property name to convert.
     * @param value
     *     The property value to convert.
     *
     * @return The type-safe Java object representing the value.
     */
    O convert(String property, Object value);
}
