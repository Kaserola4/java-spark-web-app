package com.pikolinc.routes.api;

import com.pikolinc.controllers.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class UserApiRouter implements Router {
    private static final Logger logger = LoggerFactory.getLogger(UserApiRouter.class);
    private final UserController userController;

    public UserApiRouter(UserController userController) {
        this.userController = userController;
    }

    @Override
    public void registerRoutes() {
        Spark.path("/api/v1/users", () -> {
            Spark.get("", userController::findAll);
        });
    }
}
