package com.e9pay.e9pay.web.handler;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.e9pay.e9pay.web.controller.Payload;
import com.e9pay.e9pay.web.controller.Response;
import com.e9pay.e9pay.web.exception.RestSecurityException;
import com.e9pay.e9pay.web.exception.RestValidationException;
import com.e9pay.e9pay.web.controller.RestValidationIssue;
import com.e9pay.e9pay.web.dto.RestValidationIssueDto;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.TransientObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vivek Adhikari
 * @since 4/20/2017
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(RestSecurityException.class)
    public ResponseEntity<Payload<List<RestValidationIssueDto>>> handleRestRequestException(RestSecurityException ex) {
        return Response.forbidden(Collections.singletonList(new RestValidationIssueDto(ex.getLocalizedMessage())));
    }

    @ExceptionHandler(RestValidationException.class)
    public ResponseEntity<Payload<List<RestValidationIssueDto>>> handleRestRequestException(RestValidationException ex) {
        final List<RestValidationIssueDto> issues = new ArrayList<>();

        for (RestValidationIssue issue : ex.getValidationIssues()) {
            issues.add(new RestValidationIssueDto(issue.getFieldName(), issue.getMessage()));
        }

        return Response.badRequest(issues);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Payload<List<RestValidationIssueDto>>> handleRestRequestException(ConstraintViolationException ex) {
        final List<RestValidationIssueDto> issues = new ArrayList<>();

        for (ConstraintViolation issue : ex.getConstraintViolations()) {
            issues.add(new RestValidationIssueDto(issue.getPropertyPath().toString(), issue.getMessage()));
        }

        return Response.badRequest(issues);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Payload<List<RestValidationIssueDto>>> handleRestRequestException(HttpMessageNotReadableException ex) {

        final Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof UnrecognizedPropertyException) {
            LOGGER.error("Error mapping", ex);
            final UnrecognizedPropertyException upe = (UnrecognizedPropertyException) cause;

            return Response.badRequest(Collections.singletonList(new RestValidationIssueDto(
                upe.getPropertyName(),
                String.format(
                    "Unrecognized property '%s', valid properties are '%s'",
                    upe.getPropertyName(),
                    StringUtils.join(upe.getKnownPropertyIds(), ", ")
                )
            )));
        }
        return Response.badRequest(Collections.singletonList(new RestValidationIssueDto(null, ex.getLocalizedMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Payload<List<RestValidationIssueDto>>> handleRestRequestException(MethodArgumentNotValidException ex) {

        final List<RestValidationIssueDto> issues = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            issues.add(new RestValidationIssueDto(error.getField(), error.getDefaultMessage()));
        }

        return Response.badRequest(issues);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Payload<List<RestValidationIssueDto>>> handleRestRequestException(UnsupportedOperationException ex) {
        if (ex.getMessage() != null) {
            final RestValidationIssueDto restValidationIssueDto = new RestValidationIssueDto(ex.getMessage());

            return Response.methodNotAllowed(Collections.singletonList(restValidationIssueDto));
        }

        return Response.methodNotAllowed();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Payload<List<RestValidationIssueDto>>> handleRestRequestException(Exception ex) {
        LOGGER.error("An unhandled error occurred while processing REST request.", ex);

        if (ex.getCause() instanceof TransientObjectException) {

            /*
             The caller passed in a nested object that referred to a Hibernate entity ("something"), such as:

             {"id": "41", "name": "My Object", "something": {"id": ""}, otherfield: "My Data"}

             or:

             {"id": "41", "name": "My Object", "something": {}, otherfield: "My Data"}

             In this case, we need to ensure that something is either complete:

             {"id": "41", "name": "My Object", "something": {"id": "12"}, otherfield: "My Data"}

             or not included at all:

             {"id": "41", "name": "My Object", otherfield: "My Data"}

             */
            return Response.badRequest(
                Collections.singletonList(
                    new RestValidationIssueDto(
                        "The object contained in the payload was incomplete.  If the payload includes nested objects that refer to other resources," +
                            " you'll need to make sure that all included fields are non-empty, otherwise exclude them from the payload."
                    )
                )
            );
        }

        return Response.badRequest(Collections.singletonList(new RestValidationIssueDto(ex.getLocalizedMessage())));
    }
}
