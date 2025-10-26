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
            res.type("application/json");
            res.status(404);

            Map<String, Object> response = new HashMap<>();
            response.put("error", "Not Found");
            response.put("message", e.getMessage());

            res.body(gson.toJson(response));
        });


        // 401 (Bad Request) - malformed or invalid JSON
        Spark.exception(JsonSyntaxException.class, (e, req, res) -> {
            logger.warn("Malformed Json: {}", e.getMessage());
            res.type("application/json");
            res.status(400);

            String cleanMessage = "Malformed or invalid JSON. Please check your request body syntax.";

            Map<String, Object> response = new HashMap<>();

            response.put("error", "Bad Request");
            response.put("message", cleanMessage);

            res.body(gson.toJson(response));
        });

        // 409 (Duplicated Resource)
        Spark.exception(DuplicateResourceException.class, (e, req, res) -> {
            logger.warn("Attempted to create duplicated resource: {}", e.getMessage());
            res.type("application/json");
            res.status(409);

            Map<String, Object> response = new HashMap<>();
            response.put("error", "Validation failed");
            response.put("details", e.getMessage());

            res.body(gson.toJson(response));
        });
    }

    private void initValidationExceptionHandler() {
        // 401 (Bad Request)
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
            logger.error("Unhandled exception", e); // Log internally, but don’t expose details
            res.type("application/json");
            res.status(500);

            Map<String, Object> response = new HashMap<>();
            response.put("error", "Internal Server Error");
            response.put("message", "An unexpected error occurred");

            res.body(gson.toJson(response));
        });
    }
}
