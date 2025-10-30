package com.pikolinc.routes.web;

import com.pikolinc.routes.api.Router;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class WebRouter implements Router {
    private static final MustacheTemplateEngine mustache = new MustacheTemplateEngine();

    private static void render(String path, String template, Map<String, Object> model) {
        Spark.get(path, (req, res) -> {
            Map<String, Object> viewModel = model != null ? model : new HashMap<>();
            return new ModelAndView(viewModel, template);
        }, mustache);
    }


    @Override
    public void registerRoutes() {
        render("/", "index.mustache", null);
        render("/about", "about.mustache", null);
    }
}
