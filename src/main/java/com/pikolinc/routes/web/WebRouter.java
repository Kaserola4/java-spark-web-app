package com.pikolinc.routes.web;

import com.pikolinc.routes.api.Router;
import com.pikolinc.services.ItemService;
import com.pikolinc.services.impl.ItemServiceImpl;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class WebRouter implements Router {
    private static final MustacheTemplateEngine mustache = new MustacheTemplateEngine();
    private final ItemService itemService = new ItemServiceImpl();

    @Override
    public void registerRoutes() {
        Spark.get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("items", itemService.findAll());
            return new ModelAndView(model, "index.mustache");
        }, mustache);
    }
}
