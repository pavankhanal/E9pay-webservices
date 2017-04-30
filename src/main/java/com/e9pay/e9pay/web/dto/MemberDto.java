package com.e9pay.e9pay.web.dto;

import java.util.List;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Vivek Adhikari
 * @since 4/29/2017
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDto extends BaseDto<Long> {

    private String lastName;

    private String firstName;

    private List<BeneficiaryDto> beneficiarys;
}
