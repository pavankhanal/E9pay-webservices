package com.e9pay.e9pay.api.core;

/**
 * This is the contract that every entity should follow to make sure they are Indentifiable.
 *
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
public interface Identifiable<I> {

    /**
     * @return The identifier object.
     */
    I getId();
}
