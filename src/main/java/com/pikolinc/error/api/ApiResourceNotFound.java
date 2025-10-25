package com.pikolinc.error.api;

public class ApiResourceNotFound extends RuntimeException {
    public ApiResourceNotFound(String message) {
        super(message);
    }
}
