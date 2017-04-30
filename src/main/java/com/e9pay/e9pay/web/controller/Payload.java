package com.e9pay.e9pay.web.controller;

import java.lang.reflect.Array;
import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.e9pay.e9pay.api.mapper.Summary;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * This is the standardized JSON payload object for REST called which is returned by the {@link Response} API.
 *
 * @param <R>
 *     The type of the object contained in the response body.
 *
 * @author Vivek Adhikari
 * @see Response
 */
@JsonInclude(Include.NON_NULL)
@Getter
@NoArgsConstructor
@JsonPropertyOrder({"resultCount", "page", "pageSize"})
public class Payload<R> {

    @JsonView(Summary.class)
    private R response;

    @JsonView(Summary.class)
    private Integer page;

    @JsonView(Summary.class)
    private Integer pageSize;

    @JsonView(Summary.class)
    private Integer resultCount;

    public Payload(R response) {
        this(response, null, null);
    }

    public Payload(R response, Integer page, Integer pageSize) {
        this.response = response;
        this.page = page;
        this.pageSize = pageSize;
        this.resultCount = countResults();
    }

    /**
     * This field should be {@code null} if the response code is {@link org.springframework.http.HttpStatus#NO_CONTENT}.
     *
     * @return The number of results contained in this payload.
     * //TODO check and confirm this logic once again::
     */
    private Integer countResults() {
        if (response == null) {
            return null;
        }

        if (response instanceof Collection) {
            return ((Collection) response).size();
        }

        if (response instanceof Array) {
            return Array.getLength(response);
        }

        return 1;
    }
}
