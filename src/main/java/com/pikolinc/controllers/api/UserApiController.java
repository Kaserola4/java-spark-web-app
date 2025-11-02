package com.pikolinc.controllers.api;

import com.google.gson.Gson;
import com.pikolinc.dto.request.UserCreateDto;
import com.pikolinc.services.UserService;
import com.pikolinc.util.ValidationUtil;
import spark.Request;
import spark.Response;

/**
 * REST controller for users.
 *
 * <p>Delegates user CRUD operations to {@link com.pikolinc.services.UserService}.
 */
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
        ValidationUtil.validateNotEmptyBody(request.body());
        return this.userService.insert(gson.fromJson(request.body(), UserCreateDto.class));
    }

    public long update(Request request, Response response) {
        ValidationUtil.validateNotEmptyBody(request.body());
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.userService.update(id, gson.fromJson(request.body(), UserCreateDto.class));
    }

    public long delete(Request request, Response response) {
        long id =  ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return this.userService.delete(id);
    }

    public Object options(Request request, Response response) {
        long id = ValidationUtil.validateParamFormat(":id", request.params(":id"), Long.class);
        return gson.toJson(this.userService.options(id));
    }
}