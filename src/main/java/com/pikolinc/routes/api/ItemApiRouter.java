package com.pikolinc.routes.api;

import com.pikolinc.controllers.api.ItemApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

public class ItemApiRouter implements Router {
    private static final Logger logger = LoggerFactory.getLogger(ItemApiRouter.class);
    private final ItemApiController itemController;

    public ItemApiRouter(ItemApiController itemController) {
        this.itemController = itemController;
    }

    @Override
    public void registerRoutes() {
        logger.info("Registering routes for ItemApiController");

        Spark.path("/api/v1", () -> {
            Spark.get("/items", itemController::findAll);
            Spark.get("/items/:id", itemController::findById);
            Spark.post("/items", itemController::insert);
            Spark.put("/items/:id", itemController::update);
            Spark.options("/items/:id", itemController::options);
            Spark.delete("/items/:id", itemController::delete);
        });
    }
}
