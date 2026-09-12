package io.kairos.delivery_system.modules.users.validators;

import io.kairos.delivery_system.core.exceptions.ValidationException;
import io.kairos.delivery_system.core.utilities.ValidatorUtil;
import io.kairos.delivery_system.http.request.HttpRequest;
import io.kairos.delivery_system.modules.users.dtos.UpdateUserDto;

import java.util.Map;

public class UpdateUserValidator {
    public static UpdateUserDto validate(HttpRequest request) {
        Map<String, String> body = request.body();

        if (ValidatorUtil.isNullOrEmpty(body.get("id")))
            throw new ValidationException("id is required");

        if (!ValidatorUtil.isNullOrEmpty(body.get("password"))) {
            if (body.get("password").length() < 8)
                throw new ValidationException("Password must be at least 8 characters");

            if (ValidatorUtil.isNullOrEmpty(body.get("password_confirmation")))
                throw new ValidationException("Password confirmation is required");

            if (!body.get("password_confirmation").equals(body.get("password")))
                throw new ValidationException("Passwords don't match");
        }

        if (!ValidatorUtil.isNullOrEmpty(body.get("phone")) && !ValidatorUtil.isValidPhoneNumber(body.get("phone")))
            throw new ValidationException("Please enter a valid phone number");

        if (!ValidatorUtil.isNullOrEmpty(body.get("role_id")) && ValidatorUtil.notInteger(body.get("role_id"))) {
            throw new ValidationException("Role ID must be an integer");
        }

        return new UpdateUserDto(
                Long.parseLong(body.get("id")),
                body.get("name"),
                body.get("username"),
                body.get("password"),
                body.get("email"),
                body.get("phone"),
                ValidatorUtil.isNullOrEmpty(body.get("role_id")) ? null : Long.parseLong(body.get("role_id")),
                ValidatorUtil.isNullOrEmpty(body.get("is_active")) ? null : Boolean.parseBoolean(body.get("is_active"))
        );
    }
}
