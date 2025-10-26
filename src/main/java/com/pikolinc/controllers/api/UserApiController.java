package com.pikolinc.controllers.api;

import com.google.gson.Gson;
import com.pikolinc.dto.request.UserCreateDto;
import com.pikolinc.exceptions.ValidationException;
import com.pikolinc.services.UserService;
import spark.Request;
import spark.Response;

public class UserApiController {
    private final UserService userService;
    private final Gson gson;

    public UserApiController(UserService userService) {
        this.gson = new Gson();
        this.userService = userService;
    }

    public Object findAll(Request request, Response response) {
        return this.gson.toJson(this.userService.findAll());
    }

    public Object findById(Request request, Response response) {
        return this.gson.toJson(this.userService.findById(Long.parseLong(request.params(":id"))));
    }

    public long insert(Request request, Response response) {
        String body = request.body();

        if (body == null || body.isBlank())
            throw new ValidationException("Request body cannot be empty");

        return this.userService.insert(
                gson.fromJson(body, UserCreateDto.class)
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

        return this.userService.update(id, gson.fromJson(body, UserCreateDto.class));
    }

    public long delete(Request request, Response response) {
        long id;

        try {
            id = Long.parseLong(request.params(":id"));
        }
        catch (NumberFormatException e) {
            throw new ValidationException("Invalid id format");
        }

        return this.userService.delete(id);
    }

    public Object options(Request request, Response response) {
        long id;

        try {
            id = Long.parseLong(request.params(":id"));
        }
        catch (NumberFormatException e) {
            throw new ValidationException("Invalid id format");
        }

        return gson.toJson(this.userService.options(id));
    }
}