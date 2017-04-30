package com.e9pay.e9pay.api.mapper;

/**
 * An exception thrown when a property cannot be mapped using the {@link PropertyMappings} API.
 *
 * @author Vivek Adhikari
 */
class PropertyMappingException extends RuntimeException {

    private static final long serialVersionUID = 3030890002934983157L;

    PropertyMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
