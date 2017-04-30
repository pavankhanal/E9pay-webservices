package com.e9pay.e9pay.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Vivek Adhikari
 * @since 4/22/2017
 */
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class RestValidationIssueDto {

    @JsonProperty
    private String fieldName;

    @JsonProperty
    private String message;

    public RestValidationIssueDto(String message) {
        fieldName = null;
        this.message = message;
    }
}
