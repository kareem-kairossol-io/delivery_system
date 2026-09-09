package io.kairos.delivery_system.http.router;

import io.kairos.delivery_system.http.enums.ResponseCodesEnum;
import io.kairos.delivery_system.http.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class RouterRegistry {
    public static void register() {
        Router.addRoute("POST", "/users", (request) -> {
            System.out.println(request);
            Map<String, Object> body = new HashMap<>();
            body.put("name", request.body().get("name"));

            return new HttpResponse(ResponseCodesEnum.SUCCESS, body);
        });
    }
}
