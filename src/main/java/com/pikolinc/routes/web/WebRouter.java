package com.pikolinc.routes.web;

import com.pikolinc.routes.api.Router;
import com.pikolinc.services.ItemService;
import com.pikolinc.services.OfferService;
import com.pikolinc.services.UserService;
import com.pikolinc.services.impl.ItemServiceImpl;
import com.pikolinc.services.impl.OfferServiceImpl;
import com.pikolinc.services.impl.UserServiceImpl;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class WebRouter implements Router {
    private static final MustacheTemplateEngine mustache = new MustacheTemplateEngine();
    private final ItemService itemService = new ItemServiceImpl();
    private final UserService userService = new UserServiceImpl();
    private final OfferService offerService = new OfferServiceImpl(itemService, userService);

    @Override
    public void registerRoutes() {
        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("items", itemService.findAll());
            return new ModelAndView(model, "index.mustache");
        }, mustache);

        Spark.get("/items/:id/offers", (req, res) -> {
            long itemId = Long.parseLong(req.params("id"));
            Map<String, Object> model = new HashMap<>();
            model.put("item", itemService.findById(itemId));
            model.put("offers", offerService.findByItemId(itemId));
            return new ModelAndView(model, "offer.mustache");
        }, mustache);
    }
}
