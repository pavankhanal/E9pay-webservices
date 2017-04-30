package com.e9pay.e9pay.api.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represets operations to be performed on a series of fields.
 *
 * @author Vivek Adhikari
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldOperations {

    @Getter
    private List<FieldOperation> filterOperations;
    @Getter
    private List<FieldOperation> sortOperations;

    public static FieldOperations create() {
        return new FieldOperations(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }

    /**
     * Static factory method used to create a new instance.
     *
     * @param filterOperations
     *     The filter operations to create.
     * @param sortOperations
     *     The sort operations to create.
     *
     * @return The newly initialized instance.
     */
    public static FieldOperations create(List<FieldOperation> filterOperations, List<FieldOperation> sortOperations) {
        return new FieldOperations(filterOperations, sortOperations);
    }

    /**
     * @return All of the field names involved in this query, either via filtering or sorting.
     */
    public List<String> getFieldNames() {
        final List<String> fields = new ArrayList<>();

        if (filterOperations != null) {
            for (FieldOperation op : filterOperations) {
                if (!fields.contains(op.getField())) {
                    fields.add(op.getField());
                }
            }
        }
        if (sortOperations != null) {
            for (FieldOperation op : sortOperations) {
                if (!fields.contains(op.getField())) {
                    fields.add(op.getField());
                }
            }
        }
        return Collections.unmodifiableList(fields);
    }
}
