package com.pikolinc.app.initializer;

import com.pikolinc.controllers.api.ItemApiController;
import com.pikolinc.controllers.api.UserApiController;
import com.pikolinc.routes.api.ItemApiRouter;
import com.pikolinc.routes.api.Router;
import com.pikolinc.routes.api.UserApiRouter;
import com.pikolinc.services.impl.ItemServiceImpl;
import com.pikolinc.services.impl.UserServiceImpl;
import spark.Spark;

import java.util.List;

public class RoutesInitializer implements Initializer {
    @Override
    public void init() {
        List<Router> routers = List.of(
                new UserApiRouter(new UserApiController(new UserServiceImpl())),
                new ItemApiRouter(new ItemApiController(new ItemServiceImpl()))
        );

        routers.forEach(Router::registerRoutes);

        Spark.get("/health", (req, res) -> "OK");
    }
}
