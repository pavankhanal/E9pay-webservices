package com.e9pay.e9pay.api.utils;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.e9pay.e9pay.api.mapper.ConversionCallback;
import com.e9pay.e9pay.api.mapper.PropertyMapping;
import com.e9pay.e9pay.api.mapper.PropertyMappings;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents a field, value and operation combination.
 *
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Data
@AllArgsConstructor
public class FieldOperation {

    public static final String OP_DELIMITER = "::";
    public static final String GREATER_THAN = "gt";
    public static final String GREATER_THAN_OR_NULL = "gtornull";
    public static final String GREATER_THAN_OR_EQUALS = "gte";
    public static final String GREATER_THAN_OR_EQUALS_OR_NULL = "gteornull";
    public static final String LESS_THAN = "lt";
    public static final String LESS_THAN_OR_NULL = "ltornull";
    public static final String LESS_THAN_OR_EQUALS = "lte";
    public static final String LESS_THAN_OR_EQUALS_OR_NULL = "lteornull";
    public static final String NOT_LIKE = "nlike";
    public static final String LIKE = "like";
    public static final String EQUALS = "eq";
    public static final String NOT_EQUALS = "ne";
    public static final String EQUALS_OR_IS_NULL = "eqorisnull";
    public static final String NOT_EQUALS_OR_IS_NOT_NULL = "neorisnotnull";
    public static final String IS_NULL = "isnull";
    public static final String IS_NOT_NULL = "isnotnull";
    public static final String IN = "in";
    public static final String BETWEEN = "btw";
    public static final String SORT_ASC = "sortasc";
    public static final String SORT_DESC = "sortdesc";

    private static final List<String> SANITIZED_SKIP_LIST = Arrays.asList("sort", "page", "pageSize");

    private String field;
    private String operation;
    private Object value;

    public boolean hasOperation() {
        return StringUtils.isNotBlank(operation);
    }

    public static FieldOperation sortAsc(String field) {
        return new FieldOperation(field, SORT_ASC, null);
    }

    public static FieldOperation sortDesc(String field) {
        return new FieldOperation(field, SORT_DESC, null);
    }

    public static FieldOperation gt(String field, Object value) {
        return new FieldOperation(field, GREATER_THAN, value);
    }

    public static FieldOperation gtOrNull(String field, Object value) {
        return new FieldOperation(field, GREATER_THAN_OR_NULL, value);
    }

    public static FieldOperation gte(String field, Object value) {
        return new FieldOperation(field, GREATER_THAN_OR_EQUALS, value);
    }

    public static FieldOperation gteOrNull(String field, Object value) {
        return new FieldOperation(field, GREATER_THAN_OR_EQUALS_OR_NULL, value);
    }

    public static FieldOperation lt(String field, Object value) {
        return new FieldOperation(field, LESS_THAN, value);
    }

    public static FieldOperation ltOrNull(String field, Object value) {
        return new FieldOperation(field, LESS_THAN_OR_NULL, value);
    }

    public static FieldOperation lte(String field, Object value) {
        return new FieldOperation(field, LESS_THAN_OR_EQUALS, value);
    }

    public static FieldOperation lteOrNull(String field, Object value) {
        return new FieldOperation(field, LESS_THAN_OR_EQUALS_OR_NULL, value);
    }

    public static FieldOperation like(String field, Object value) {
        return new FieldOperation(field, LIKE, value);
    }

    public static FieldOperation nlike(String field, Object value) {
        return new FieldOperation(field, NOT_LIKE, value);
    }

    public static FieldOperation eq(String field, Object value) {
        return new FieldOperation(field, EQUALS, value);
    }

    public static FieldOperation ne(String field, Object value) {
        return new FieldOperation(field, NOT_EQUALS, value);
    }

    public static FieldOperation eqOrIsNull(String field, Object value) {
        return new FieldOperation(field, EQUALS_OR_IS_NULL, value);
    }

    public static FieldOperation neOrIsNotNull(String field, Object value) {
        return new FieldOperation(field, NOT_EQUALS_OR_IS_NOT_NULL, value);
    }

    public static FieldOperation isNull(String field) {
        return new FieldOperation(field, IS_NULL, null);
    }

    public static FieldOperation isNotNull(String field) {
        return new FieldOperation(field, IS_NOT_NULL, null);
    }

    public static FieldOperation in(String field, Object[] values) {
        return new FieldOperation(field, IN, values);
    }

    public static FieldOperation between(String field, Object[] values) {
        return new FieldOperation(field, BETWEEN, values);
    }

    /**
     * Validates the {@link String} representation of a field operation.
     *
     * @param operation
     *     The operation to validate.
     *
     * @return true if the supplied operation is valid.
     */
    public static boolean validate(String operation) {
        if (StringUtils.isNotBlank(operation)) {
            switch (operation) {
            case GREATER_THAN:
            case GREATER_THAN_OR_NULL:
            case GREATER_THAN_OR_EQUALS:
            case GREATER_THAN_OR_EQUALS_OR_NULL:
            case LESS_THAN:
            case LESS_THAN_OR_NULL:
            case LESS_THAN_OR_EQUALS:
            case LESS_THAN_OR_EQUALS_OR_NULL:
            case LIKE:
            case NOT_LIKE:
            case EQUALS:
            case NOT_EQUALS:
            case EQUALS_OR_IS_NULL:
            case NOT_EQUALS_OR_IS_NOT_NULL:
            case IS_NULL:
            case IS_NOT_NULL:
            case IN:
            case BETWEEN:
            case SORT_ASC:
            case SORT_DESC:
                return true;
            default:
                return false;
            }
        }
        return false;
    }

    /**
     * Given a map of field to operator values, create a {@link List} of FieldOperation instances.  This method will not perform any data-type
     * conversion on the input map and will treat all field values as {@link String} instances.
     *
     * @param fieldOpValues
     *     A mapping of field name -> field values.
     * @param propertyMappings
     *     The {@link PropertyMappings} instance that is used to resolve which fields are valid.
     *
     * @return A {@link List} that has been validated against the supplied {@link PropertyMappings}.
     */
    public static List<FieldOperation> createFromFilter(
        Map<String, String> fieldOpValues,
        PropertyMappings propertyMappings
    ) {
        return createFromFilter(fieldOpValues, propertyMappings, new ConversionCallback<Object>() {
            @Override
            public Object convert(String property, Object value) {
                return value;
            }
        });
    }

    /**
     * Given a map of field to operator values, create a {@link List} of FieldOperation instances.  This method accepts a custom {@link
     * ConversionCallback} instance to perform any required type conversions.
     *
     * @param fieldOpValues
     *     A mapping of field name -> field values.
     * @param propertyMappings
     *     The {@link PropertyMappings} instance that is used to resolve which fields are valid.
     * @param callback
     *     A custom {@link ConversionCallback} supplied to convert the String representations of the fields in the input map to specific Java-types.
     *
     * @return A {@link List} that has been validated against the supplied {@link PropertyMappings}.
     */
    public static List<FieldOperation> createFromFilter(
        Map<String, String> fieldOpValues,
        PropertyMappings propertyMappings,
        ConversionCallback callback
    ) {
        final List<FieldOperation> fieldOperations = new ArrayList<>();

        if (MapUtils.isNotEmpty(fieldOpValues)) {

            final LinkedHashMap<String, String> cleanFields = sanitizeFields(fieldOpValues, propertyMappings);


            for (Entry<String, String> entry : cleanFields.entrySet()) {
                final String field = entry.getKey();
                String operation = substringBeforeLast(substringAfter(entry.getValue(), OP_DELIMITER), OP_DELIMITER);
                String value = entry.getValue();

                if (StringUtils.isBlank(operation)) {
                    operation = EQUALS;
                } else {
                    value = substringAfter(substringAfter(entry.getValue(), OP_DELIMITER), OP_DELIMITER);
                }

                Object convertedValue = value;

                if (callback != null) {
                    convertedValue = callback.convert(field, processCollectionValues(value, operation));
                }

                if (validate(operation)) {
                    fieldOperations.add(new FieldOperation(field, operation, convertedValue));
                }
            }
        }
        return fieldOperations;
    }

    private static Object processCollectionValues(String value, String operation) {

        switch (operation) {

        case BETWEEN:
        case IN:
            return value.split(",");

        default:
            return value;
        }
    }

    /**
     * Parse a sort specification string and create the appropriate field operations from its contents.
     *
     * @param sortSpec
     *     The String containing which fields should be sorted and in what direction.  The spec follows the folloing format:
     *     [+|-]field[,[+|-]fieldN].
     * @param propertyMappings
     *     The {@link PropertyMappings} instance that is used to resolve which fields are valid.
     *
     * @return A {@link List} of validated sort fields.
     */
    public static List<FieldOperation> createFromSort(String sortSpec, PropertyMappings propertyMappings) {

        final List<FieldOperation> fieldOperations = new ArrayList<>();

        if (StringUtils.isNotBlank(sortSpec)) {

            final String[] fieldArray = StringUtils.split(sortSpec, ",");

            for (String field : fieldArray) {
                if (field.length() > 2) {
                    final String key = StringUtils.trim(field.substring(1));

                    if (propertyMappings.containsSource(key)) {

                        final PropertyMapping mapping = propertyMappings.get(key);

                        if (field.startsWith("+")) {
                            fieldOperations.add(new FieldOperation(mapping.getTargetProperty(), SORT_ASC, null));
                        } else if (field.startsWith("-")) {
                            fieldOperations.add(new FieldOperation(mapping.getTargetProperty(), SORT_DESC, null));
                        }
                    }
                }
            }
        }
        return fieldOperations;
    }

    /**
     * This is a bridge method between the service API and the DTO/entity mapping framework.  This method will take the filter criteria from the
     * controller endpoint and clean it up so that the field names correspond to the names that the service API is expecting.  As a side-effect of
     * this, its also responsible for rejecting any fields that haven't been published on the DTO.
     *
     * @return A {@link Map} containing the field to value criteria that should be supplied to the service API.
     */
    private static <T> LinkedHashMap<String, T> sanitizeFields(Map<String, T> input, PropertyMappings propertyMappings) {

        final LinkedHashMap<String, T> outputFields = new LinkedHashMap<>();

        if (MapUtils.isNotEmpty(input)) {
            for (Entry<String, T> entry : input.entrySet()) {

                final T value = entry.getValue();

                if (value != null) {

                    if (value instanceof String) {
                        if (StringUtils.isBlank((String) value)) {
                            continue;
                        }
                    }

                    final String inputField = entry.getKey();

                    if (!SANITIZED_SKIP_LIST.contains(inputField)) {

                        // This completes the mapping to the internal name but also filters out any fields that we
                        // shouldn't expose which is a security requirement.
                        final PropertyMapping foundOutputField = propertyMappings.get(inputField);
                        if (foundOutputField != null) {
                            outputFields.put(foundOutputField.getTargetProperty(), value);
                        }
                    }
                }
            }
        }

        return outputFields;
    }
}
