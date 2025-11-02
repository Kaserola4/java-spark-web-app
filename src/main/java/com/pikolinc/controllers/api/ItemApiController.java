package com.pikolinc.controllers.api;

import com.google.gson.Gson;
import com.pikolinc.services.ItemService;
import com.pikolinc.util.ValidationUtil;
import spark.Request;
import spark.Response;

import com.pikolinc.dto.request.ItemCreateDto;

/**
 * REST controller for items.
 *
 * <p>Exposes CRUD endpoints for {@link com.pikolinc.domain.Item}.
 * It validates request bodies and delegates business logic to {@link com.pikolinc.services.ItemService}.
 */
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
        ValidationUtil.validateNotEmptyBody(request.body());
        return this.itemService.insert(gson.fromJson(request.body(), ItemCreateDto.class));
    }

    public long update(Request request, Response response) {
        ValidationUtil.validateNotEmptyBody(request.body());
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.itemService.update(id, gson.fromJson(request.body(), ItemCreateDto.class));
    }

    public long delete(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.itemService.delete(id);
    }

    public Object options(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return gson.toJson(this.itemService.options(id));
    }
}
