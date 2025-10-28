package com.pikolinc.controllers.api;

import com.google.gson.Gson;
import com.pikolinc.services.ItemService;
import spark.Request;
import spark.Response;

import com.pikolinc.dto.request.ItemCreateDto;
import com.pikolinc.exceptions.ValidationException;

public class ItemApiController {
    private final ItemService itemService;
    private final Gson gson;

    public ItemApiController(ItemService itemService) {
        this.gson = new Gson();
        this.itemService = itemService;
    }

    public Object findAll(Request request, Response response) {
        return this.gson.toJson(this.itemService.findAll());
    }

    public Object findById(Request request, Response response) {
        return this.gson.toJson(this.itemService.findById(Long.parseLong(request.params(":id"))));
    }

    public long insert(Request request, Response response) {
        String body = request.body();

        if (body == null || body.isBlank())
            throw new ValidationException("Request body cannot be empty");

        return this.itemService.insert(
                gson.fromJson(body, ItemCreateDto.class)
        );
    }

    public long update(Request request, Response response) {
        String body = request.body();

        if (body == null || body.isBlank())
            throw new ValidationException("Request body cannot be empty");

        long id;

        try {
            id = Long.parseLong(request.params(":id"));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid id format");
        }

        return this.itemService.update(id, gson.fromJson(body, ItemCreateDto.class));
    }

    public long delete(Request request, Response response) {
        long id;

        try {
            id = Long.parseLong(request.params(":id"));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid id format");
        }

        return this.itemService.delete(id);
    }

    public Object options(Request request, Response response) {
        long id;

        try {
            id = Long.parseLong(request.params(":id"));
        } catch (NumberFormatException e) {
            throw new ValidationException("Invalid id format");
        }

        return gson.toJson(this.itemService.options(id));
    }
}
