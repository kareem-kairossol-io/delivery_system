package io.kairos.delivery_system.modules.users.controller;

import io.kairos.delivery_system.http.enums.ResponseCodesEnum;
import io.kairos.delivery_system.http.request.HttpRequest;
import io.kairos.delivery_system.http.response.HttpResponse;
import io.kairos.delivery_system.modules.users.dtos.CreateUserDto;
import io.kairos.delivery_system.modules.users.dtos.UpdateUserDto;
import io.kairos.delivery_system.modules.users.dtos.UserDto;
import io.kairos.delivery_system.modules.users.services.UserService;
import io.kairos.delivery_system.modules.users.validators.CreateUserValidator;
import io.kairos.delivery_system.modules.users.validators.UpdateUserValidator;

import java.util.LinkedHashMap;
import java.util.Map;

public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    public HttpResponse createUser(HttpRequest request)  {
        // validate request
        CreateUserDto user = CreateUserValidator.validate(request);

        // create user
        UserDto createdUser = this.userService.save(user);

        // make response
        Map<String,Object> res = new LinkedHashMap<>();

        res.put("message", "User created successfully");
        res.put("user", createdUser.toMap());

        return new HttpResponse(ResponseCodesEnum.CREATED, res);
    }

    public HttpResponse updateUser(HttpRequest request)  {
        // validate request
        UpdateUserDto user = UpdateUserValidator.validate(request);

        // Update user
        UserDto updatedUser = this.userService.update(user);

        // make response
        Map<String,Object> res = new LinkedHashMap<>();

        res.put("message", "User updated successfully");
        res.put("user", updatedUser.toMap());

        return new HttpResponse(ResponseCodesEnum.SUCCESS, res);
    }
}
