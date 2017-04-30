package com.e9pay.e9pay.web.dto;

import lombok.Data;

import com.e9pay.e9pay.api.core.Identifiable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The base class for data-transfer objects that are used to expose a contract to the public API.
 *
 * @author Vivek Adhikari
 */
@Data
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDto<T> implements Identifiable<T> {

    /**
     * The unique ID for this specific record.
     */
    private T id;
}
