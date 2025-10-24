package com.pikolinc;


import com.google.gson.Gson;
import com.pikolinc.config.Database;
import com.pikolinc.controllers.UserController;
import com.pikolinc.routes.api.Router;
import com.pikolinc.routes.api.UserApiRouter;
import com.pikolinc.services.impl.UserServiceImpl;
import spark.Spark;

import java.util.List;

public class JavaSparkWebApp {
    public static void main(String[] args) {
        Database.init();

        Spark.port(8080);

        Spark.before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            response.header("Content-Type", "application/json");
        });

        Gson gson = new Gson();

        List<Router> routers = List.of(
                new UserApiRouter(new UserController(gson, new UserServiceImpl()))
                // new ItemsApiRouter(newItemController(gson, new ItemServiceImpl()))
        );

        routers.forEach(Router::registerRoutes);

        Spark.get("/health", ((request, response) -> "OK"));

        System.out.println("Server is up!");
    }
}