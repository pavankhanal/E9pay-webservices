package com.e9pay.e9pay.web.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Vivek Adhikari
 * @since 4/21/2017
 */

@Getter
@AllArgsConstructor
@SuppressWarnings("PMD.UnusedPrivateField")
public class RestValidationIssue {
    private final String fieldName;
    private final String message;
}
