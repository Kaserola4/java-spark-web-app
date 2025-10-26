package com.pikolinc.app.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class MiddlewaresInitializer implements Initializer {
    private static final Logger logger = LoggerFactory.getLogger(MiddlewaresInitializer.class);

    @Override
    public void init() {
        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Content-Type", "application/json");
            logger.info("Incoming request: {} {}", request.requestMethod(), request.pathInfo());
        });


        Spark.afterAfter((request, response) -> {
            logger.info("Completed {} {} with status {}", request.requestMethod(), request.pathInfo(), response.status());
        });
    }
}
