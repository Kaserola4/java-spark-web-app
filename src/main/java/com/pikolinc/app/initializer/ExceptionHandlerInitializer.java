package com.pikolinc.app.initializer;

import com.pikolinc.error.api.ApiResourceNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class ExceptionHandlerInitializer implements Initializer {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerInitializer.class);

    @Override
    public void init() {
        Spark.exception(RuntimeException.class, (e, req, res) -> {
            logger.error("Exception caught in ExceptionHandler", e);
            res.type("application/json");
            res.status(500);
            res.body(e.getMessage());
        });

        ExceptionHandlerInitializer.initApiErrors();
    }

    private static void initApiErrors() {
        Spark.exception(ApiResourceNotFound.class, (e, req, res) -> {
            logger.error("Exception caught in ApiResourceNotFound", e);
            res.type("application/json");
            res.status(404);
            res.body(e.getMessage());
        });
    }
}
