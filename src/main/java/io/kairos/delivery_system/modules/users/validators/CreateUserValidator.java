package io.kairos.delivery_system.modules.users.validators;

import io.kairos.delivery_system.core.exceptions.ValidationException;
import io.kairos.delivery_system.core.utilities.ValidatorUtil;
import io.kairos.delivery_system.http.request.HttpRequest;
import io.kairos.delivery_system.modules.users.dtos.CreateUserDto;

import java.util.Map;
import java.util.Objects;

public class CreateUserValidator {
    public static CreateUserDto validate(HttpRequest request) {
        Map<String, String> body = request.body();

        if (ValidatorUtil.isNullOrEmpty(body.get("name")))
            throw new ValidationException("Name is required");

        if (body.get("email") == null || Objects.equals(body.get("email"), ""))
            throw new ValidationException("Email address is required");

        if (!ValidatorUtil.isValidEmail(body.get("email")))
            throw new ValidationException("Please enter a valid email address");

        if (body.get("password") == null || Objects.equals(body.get("password"), ""))
            throw new ValidationException("Password is required");

        if (body.get("password").length() < 8)
            throw new ValidationException("Password must be at least 8 characters");

        if (body.get("password_confirmation") == null || Objects.equals(body.get("password_confirmation"), ""))
            throw new ValidationException("Password confirmation is required");

        if (!body.get("password_confirmation").equals(body.get("password")))
            throw new ValidationException("Passwords don't match");

        if (body.get("phone") == null || Objects.equals(body.get("phone"), ""))
            throw new ValidationException("Phone number is required");

        if (!ValidatorUtil.isValidPhoneNumber(body.get("phone")))
            throw new ValidationException("Please enter a valid phone number");

        if (body.get("role_id") == null || Objects.equals(body.get("role_id"), "")) {
            throw new ValidationException("Role ID is required");
        }

        if (ValidatorUtil.notInteger(body.get("role_id"))) {
            throw new ValidationException("Role ID must be an integer");
        }

        return new CreateUserDto(
            body.get("name"),
            body.get("username"),
            body.get("password"),
            body.get("email"),
            body.get("phone"),
            Long.parseLong(body.get("role_id"))
        );
    }
}
