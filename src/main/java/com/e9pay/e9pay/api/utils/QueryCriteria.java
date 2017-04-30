package com.e9pay.e9pay.api.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * Represents a collection of criteria that are used to create a query.
 *
 * @author Vivek Adhikari
 * @since 4/21/2017
 */
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryCriteria {

    public static final QueryCriteria ALL = create(FieldOperations.create(), 1, Integer.MAX_VALUE);

    private FieldOperations fieldOperations;
    private Integer page;
    private Integer pageSize;

    /**
     * Static factory method used to create a new instance.
     *
     * @param fieldOperations
     *     The operations to perform.
     * @param page
     *     The starting page.
     * @param pageSize
     *     The maximum number of records for the page.
     *
     * @return A newly initialized instance.
     */
    public static QueryCriteria create(FieldOperations fieldOperations, Integer page, Integer pageSize) {
        return new QueryCriteria(fieldOperations, page, pageSize);
    }
}
