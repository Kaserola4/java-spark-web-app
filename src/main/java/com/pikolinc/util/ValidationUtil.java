package com.pikolinc.util;

import jakarta.validation.*;
import com.pikolinc.exceptions.ValidationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public class ValidationUtil {
    private static final Validator validator;
    private static final Map<Class<?>, Function<String, ?>> PARSERS = new HashMap<>();

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        PARSERS.put(Long.class, Long::parseLong);
        PARSERS.put(Integer.class, Integer::parseInt);
        PARSERS.put(Double.class, Double::parseDouble);
        PARSERS.put(Float.class, Float::parseFloat);
        PARSERS.put(UUID.class, UUID::fromString);
        PARSERS.put(String.class, s -> s); // allows simple string params
    }

    /**
     * Validates an object and throws ValidationException if violations exist
     *
     * @param object The object to validate
     * @throws ValidationException if validation fails
     */
    public static <T> void validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }

    /**
     * Validates an object with specific validation groups
     *
     * @param object The object to validate
     * @param groups Validation groups to apply
     * @throws ValidationException if validation fails
     */
    public static <T> void validate(T object, Class<?>... groups) {
        Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }

    /**
     * Validates a specific property of an object
     *
     * @param object       The object containing the property
     * @param propertyName The property name to validate
     * @throws ValidationException if validation fails
     */
    public static <T> void validateProperty(T object, String propertyName) {
        Set<ConstraintViolation<T>> violations = validator.validateProperty(object, propertyName);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }

    /**
     * Validates an object and returns true/false without throwing
     *
     * @param object The object to validate
     * @return true if valid, false otherwise
     */
    public static <T> boolean isValid(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        return violations.isEmpty();
    }

    /**
     * Gets violations without throwing exception
     *
     * @param object The object to validate
     * @return Set of constraint violations
     */
    public static <T> Set<ConstraintViolation<T>> getViolations(T object) {
        return validator.validate(object);
    }

    /**
     * Checks if the request body is empty or null.
     * This is useful before running bean validation.
     *
     * @param body The request body object (after deserialization)
     * @throws ValidationException if the body is null or has no properties
     */
    public static void validateNotEmptyBody(Object body) {
        if (body == null) {
            throw new ValidationException("Request body cannot be null or empty.");
        }

        // check if it's an empty bean (all fields null)
        boolean allFieldsNull = java.util.Arrays.stream(body.getClass().getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .allMatch(f -> {
                    try {
                        return f.get(body) == null;
                    } catch (IllegalAccessException e) {
                        return true; // treat inaccessible as null
                    }
                });

        if (allFieldsNull) {
            throw new ValidationException("Request body cannot be empty.");
        }
    }

    /**
     * Validates that a request parameter matches the expected type
     * and returns its parsed value.
     *
     * @param paramName  The name of the parameter (for error messages)
     * @param paramValue The raw string value of the parameter
     * @param type       The expected type (e.g., Long.class, UUID.class)
     * @param <T>        The generic return type
     * @return The parsed value of the given type
     * @throws ValidationException if parsing fails or type is unsupported
     */
    @SuppressWarnings("unchecked")
    public static <T> T validateParamFormat(String paramName, String paramValue, Class<T> type) {
        if (paramValue == null || paramValue.isBlank()) {
            throw new ValidationException("Missing or empty parameter: " + paramName);
        }

        Function<String, ?> parser = PARSERS.get(type);

        if (parser == null) {
            throw new ValidationException("Unsupported parameter type: " + type.getSimpleName());
        }

        try {
            return (T) parser.apply(paramValue);
        } catch (Exception e) {
            throw new ValidationException(
                    "Invalid format for parameter '" + paramName + "': expected " + type.getSimpleName()
            );
        }
    }

}