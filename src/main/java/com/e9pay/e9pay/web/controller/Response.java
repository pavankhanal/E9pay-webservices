package com.e9pay.e9pay.web.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class is used to produce structured {@link ResponseEntity} objects which contain the results of a REST operation.
 *
 * @author Vivek Adhikari
 */
public class Response {

    public static <D> ResponseEntity<D> with(HttpStatus status) {
        return new ResponseEntity<>(status);
    }

    public static <D> ResponseEntity<D> with(HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(headers, status);
    }

    public static <D> ResponseEntity<Payload<D>> with(D dto, HttpStatus status) {
        return new ResponseEntity<>(new Payload<>(dto), status);
    }

    public static <D> ResponseEntity<Payload<D>> with(D dto, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(new Payload<>(dto), headers, status);
    }

    public static <D> ResponseEntity<Payload<D>> with(Payload<D> payload, HttpStatus status) {
        return new ResponseEntity<>(payload, status);
    }

    public static <D> ResponseEntity<Payload<D>> with(Payload<D> payload, HttpHeaders headers, HttpStatus status) {
        return new ResponseEntity<>(payload, headers, status);
    }

    public static <D> ResponseEntity<D> ok() {
        return with(HttpStatus.OK);
    }

    public static <D> ResponseEntity<Payload<D>> ok(D dto) {
        return with(dto, HttpStatus.OK);
    }

    public static <D> ResponseEntity<Payload<D>> ok(D dto, Integer page, Integer pageSize) {
        return with(new Payload<>(dto, page, pageSize), HttpStatus.OK);
    }

    public static <D> ResponseEntity<D> ok(HttpHeaders headers) {
        return with(headers, HttpStatus.OK);
    }

    public static <D> ResponseEntity<Payload<D>> ok(D dto, HttpHeaders headers) {
        return with(dto, headers, HttpStatus.OK);
    }

    public static <D> ResponseEntity<D> notFound() {
        return with(HttpStatus.NOT_FOUND);
    }

    public static <D> ResponseEntity<D> notFound(HttpHeaders headers) {
        return with(headers, HttpStatus.NOT_FOUND);
    }

    public static <D> ResponseEntity<D> badRequest() {
        return with(HttpStatus.BAD_REQUEST);
    }

    public static <D> ResponseEntity<Payload<D>> badRequest(D dto) {
        return with(dto, HttpStatus.BAD_REQUEST);
    }

    public static <D> ResponseEntity<D> badRequest(HttpHeaders headers) {
        return with(headers, HttpStatus.BAD_REQUEST);
    }

    public static <D> ResponseEntity<D> internalError() {
        return with(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <D> ResponseEntity<D> internalError(HttpHeaders headers) {
        return with(headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static <D> ResponseEntity<D> noContent() {
        return with(HttpStatus.NO_CONTENT);
    }

    public static <D> ResponseEntity<D> noContent(HttpHeaders headers) {
        return with(headers, HttpStatus.NO_CONTENT);
    }

    public static <D> ResponseEntity<D> forbidden() {
        return with(HttpStatus.FORBIDDEN);
    }

    public static <D> ResponseEntity<D> forbidden(HttpHeaders headers) {
        return with(headers, HttpStatus.FORBIDDEN);
    }

    public static <D> ResponseEntity<Payload<D>> forbidden(D dto) {
        return with(dto, HttpStatus.FORBIDDEN);
    }

    public static <D> ResponseEntity<Payload<D>> methodNotAllowed(D dto) {
        return with(dto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    public static <D> ResponseEntity<D> methodNotAllowed(HttpHeaders headers) {
        return with(headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    public static <D> ResponseEntity<Payload<D>> methodNotAllowed() {
        return with(HttpStatus.METHOD_NOT_ALLOWED);
    }
}
