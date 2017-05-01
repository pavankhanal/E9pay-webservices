package com.e9pay.e9pay.web.dto;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends BaseDto<Long> {
shdfafffffffffffffffffffffffffffffffffffffffdddddddddddddddddddd
    private String userId;

    private String userName;

    private String userNameInKorean;

    private String mobile1;

    private String mobile2;

    private String email;

    private String role;
}
