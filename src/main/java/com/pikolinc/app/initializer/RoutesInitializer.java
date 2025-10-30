package com.pikolinc.app.initializer;

import com.pikolinc.controllers.api.ItemApiController;
import com.pikolinc.controllers.api.OfferApiController;
import com.pikolinc.controllers.api.UserApiController;
import com.pikolinc.routes.api.ItemApiRouter;
import com.pikolinc.routes.api.OfferApiRouter;
import com.pikolinc.routes.api.Router;
import com.pikolinc.routes.api.UserApiRouter;
import com.pikolinc.routes.web.WebRouter;
import com.pikolinc.services.ItemService;
import com.pikolinc.services.OfferService;
import com.pikolinc.services.UserService;
import com.pikolinc.services.impl.ItemServiceImpl;
import com.pikolinc.services.impl.OfferServiceImpl;
import com.pikolinc.services.impl.UserServiceImpl;
import spark.Spark;

import java.util.List;

public class RoutesInitializer implements Initializer {
    @Override
    public void init() {

        UserService userService = new UserServiceImpl();
        ItemService itemService = new ItemServiceImpl();
        OfferService offerService = new OfferServiceImpl(itemService, userService);

        UserApiController userController = new UserApiController(userService);
        ItemApiController itemController = new ItemApiController(itemService);
        OfferApiController offerController = new OfferApiController(offerService);


        List<Router> routers = List.of(
                new UserApiRouter(userController),
                new ItemApiRouter(itemController),
                new OfferApiRouter(offerController),
                new WebRouter()
        );

        routers.forEach(Router::registerRoutes);

        Spark.get("/health", (req, res) -> "OK");
    }
}
