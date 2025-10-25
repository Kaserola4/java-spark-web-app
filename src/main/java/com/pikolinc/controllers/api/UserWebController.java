package com.pikolinc.controllers.api;

import com.google.gson.Gson;
import com.pikolinc.dto.request.UserCreateDto;
import com.pikolinc.exceptions.ValidationException;
import com.pikolinc.services.UserService;
import spark.Request;
import spark.Response;

public class UserWebController {
    private final UserService userService;
    private final Gson gson;

    public UserWebController(UserService userService) {
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
                gson.fromJson(request.body(), UserCreateDto.class)
        );
    }
}