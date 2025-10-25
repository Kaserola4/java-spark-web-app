package com.pikolinc.exceptions;

import jakarta.validation.ConstraintViolation;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ValidationException extends RuntimeException {
    private final List<String> errors;

    public ValidationException(Set<? extends ConstraintViolation<?>> violations) {
        super("Validation failed");
        this.errors = violations.stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.toList());
    }

    public ValidationException(String message) {
        super(message);
        this.errors = List.of(message);
    }
}



