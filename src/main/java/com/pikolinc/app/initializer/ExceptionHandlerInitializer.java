package com.pikolinc.app.initializer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pikolinc.exceptions.ValidationException;
import com.pikolinc.exceptions.api.ApiResourceNotFoundException;
import com.pikolinc.exceptions.api.DuplicateResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures global exception handlers for the Spark application.
 *
 * <p>Maps domain and framework exceptions to HTTP responses (400, 404, 409, 500).
 * Serializes error responses as JSON.
 */
public class ExceptionHandlerInitializer implements Initializer {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerInitializer.class);
    private static final Gson gson = new Gson();

    @Override
    public void init() {
        initGenericExceptionHandler();
        initValidationExceptionHandler();
        initApiExceptionHandler();
    }

    private void initApiExceptionHandler() {
        // 404 (Not found)
        Spark.exception(ApiResourceNotFoundException.class, (e, req, res) -> {
            logger.warn("Resource not found: {}", e.getMessage());
            sendJsonResponse(res, 404, "Not Found", e.getMessage());
        });

        // 400 (Bad Request) - malformed or invalid JSON
        Spark.exception(JsonSyntaxException.class, (e, req, res) -> {
            logger.warn("Malformed Json: {}", e.getMessage());
            String cleanMessage = "Malformed or invalid JSON. Please check your request body syntax.";
            sendJsonResponse(res, 400, "Bad Request", cleanMessage);
        });

        // 409 (Duplicated Resource)
        Spark.exception(DuplicateResourceException.class, (e, req, res) -> {
            logger.warn("Attempted to create duplicated resource: {}", e.getMessage());
            sendJsonResponse(res, 409, "Validation failed", e.getMessage());
        });
    }

    private void initValidationExceptionHandler() {
        // 400 (Bad Request)
        Spark.exception(ValidationException.class, (e, req, res) -> {
            logger.warn("Validation failed: {}", e.getErrors());
            res.type("application/json");
            res.status(400);

            Map<String, Object> response = new HashMap<>();
            response.put("error", "Validation failed");
            response.put("details", e.getErrors());

            res.body(gson.toJson(response));
        });
    }

    private void initGenericExceptionHandler() {
        Spark.exception(RuntimeException.class, (e, req, res) -> {
            logger.error("Unhandled exception", e);
            sendJsonResponse(res, 500, "Internal Server Error", "An unexpected error occurred");
        });
    }

    private void sendJsonResponse(spark.Response res, int status, String error, String message) {
        res.type("application/json");
        res.status(status);

        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        response.put("message", message);

        res.body(gson.toJson(response));
    }
}