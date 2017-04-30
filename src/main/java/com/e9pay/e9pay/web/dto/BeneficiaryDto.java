package com.e9pay.e9pay.web.dto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BeneficiaryDto extends BaseDto<Long> {

    private String name;

    private String address;
}
