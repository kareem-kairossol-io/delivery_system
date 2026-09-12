package io.kairos.delivery_system.core.exceptions;

import io.kairos.delivery_system.http.enums.ResponseCodesEnum;
import io.kairos.delivery_system.http.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler {

    public static HttpResponse handle(Exception exception) {
        Map<String, Object> body = new HashMap<>();

        if (exception instanceof ApplicationException applicationException) {
            body.put("message", applicationException.getMessage());

            return new HttpResponse(
                applicationException.statusCode(),
                body
            );
        }

        exception.printStackTrace();

        body.put("message", "Internal server error");

        return new HttpResponse(ResponseCodesEnum.INTERNAL_SERVER_ERROR, body);
    }
}