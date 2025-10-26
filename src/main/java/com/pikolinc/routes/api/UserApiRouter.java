package com.pikolinc.routes.api;

import com.pikolinc.controllers.api.UserWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class UserApiRouter implements Router {
    private static final Logger logger = LoggerFactory.getLogger(UserApiRouter.class);
    private final UserWebController userController;

    public UserApiRouter(UserWebController userController) {
        this.userController = userController;
    }

    @Override
    public void registerRoutes() {
        Spark.path("/api/v1", () -> {
            Spark.get("/users", userController::findAll);
            Spark.get("/users/:id", userController::findById);
            Spark.post("/users", userController::insert);
            Spark.put("/users/:id", userController::update);
        });
    }
}
