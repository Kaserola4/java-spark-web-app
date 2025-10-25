package com.pikolinc.exceptions.api;

public class ApiResourceNotFound extends RuntimeException {
    public ApiResourceNotFound(String message) {
        super(message);
    }
}
