package com.e9pay.e9pay.web.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

import com.e9pay.e9pay.web.controller.RestValidationIssue;

/**
 * Exception thrown when there is an issue with the a REST request
 *
 * @author Vivek Adhikari
 */
@Getter
public class RestValidationException extends RuntimeException {

    private static final long serialVersionUID = -7476041306524603925L;

    private final List<RestValidationIssue> validationIssues = new ArrayList<>();

    public RestValidationException() {
        super("Field validation issues: ");
    }

    public RestValidationException(List<RestValidationIssue> restValidationIssueList) {
        super("Field validation issues: ");
        validationIssues.addAll(restValidationIssueList);
    }

    public RestValidationException(RestValidationIssue... restValidationIssues) {
        this(Arrays.asList(restValidationIssues));
    }

    @Override
    public String getMessage() {

        final StringBuilder message = new StringBuilder(super.getMessage());

        for (final RestValidationIssue validationIssue : validationIssues) {
            message.append(" ").append(validationIssue.getMessage());
        }

        return message.toString();
    }

    /**
     * Construct a new {@link RestValidationException} with a single issue.
     *
     * @param field
     *     The field at issue.
     * @param message
     *     The validation error message.
     *
     * @return The newly created {@link RestValidationException}.
     */
    public static RestValidationException withIssue(String field, String message) {
        return new RestValidationException(new RestValidationIssue(field, message));
    }
}