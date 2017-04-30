package com.e9pay.e9pay.web.exception;

/**
 * Exception thrown then an authorization or authentication issue exists during a REST call.
 *
 * @author Vivek Adhikari
 */
public class RestSecurityException extends RuntimeException {

    public RestSecurityException() {
    }

    public RestSecurityException(String message) {
        super(message);
    }

    public RestSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestSecurityException(Throwable cause) {
        super(cause);
    }

    public RestSecurityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
