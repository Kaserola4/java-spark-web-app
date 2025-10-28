package com.pikolinc.controllers.api;

import com.google.gson.Gson;
import com.pikolinc.services.OfferService;
import spark.Request;
import spark.Response;

public class OfferApiController {
    private final OfferService offerService;
    private final Gson gson;

    public OfferApiController(OfferService offerService) {
        this.gson = new Gson();
        this.offerService = offerService;
    }

    public Object findAll(Request request, Response response) {
        return this.gson.toJson(this.offerService.findAll());
    }

    public Object findById(Request request, Response response) {
        return this.gson.toJson(this.offerService.findById(Long.parseLong(request.params(":id"))));
    }
}
