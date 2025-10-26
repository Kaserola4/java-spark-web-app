package com.pikolinc.exceptions.api;

public class ApiResourceNotFoundException extends RuntimeException {
    public ApiResourceNotFoundException(String message) {
        super(message);
    }

    public ApiResourceNotFoundException(String resource, Long id) {
        super(String.format("%s not found with id: %d", resource, id));
    }

    public ApiResourceNotFoundException(String resource, String field, String value) {
        super(String.format("%s not found with %s: %s", resource, field, value));
    }
}
