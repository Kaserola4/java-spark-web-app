package com.pikolinc.controllers;

import com.google.gson.Gson;
import com.pikolinc.services.UserService;
import spark.Request;
import spark.Response;

public class UserController {
    private final UserService userService;

    public UserController(Gson gson, UserService userService) {
        this.userService = userService;
    }

    public Object findAll(Request request, Response response) {
        response.type("application/json");
        return this.userService.findAll();
    }
}