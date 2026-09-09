package io.kairos.delivery_system.http.router;

import io.kairos.delivery_system.http.enums.ResponseCodesEnum;
import io.kairos.delivery_system.http.request.HttpRequest;
import io.kairos.delivery_system.http.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private static final Map<RouteKey, Handler> routes = new HashMap<>();

    public static HttpResponse route(
            String method,
            String path,
            HttpRequest request
    ) {
        Handler handler = routes.get(
            new RouteKey(method, path)
        );

        if (handler != null) {
            return handler.handle(request);
        }

        return new HttpResponse(ResponseCodesEnum.NOT_FOUND);
    }
    public static void addRoute(String method, String path, Handler handler) {
        routes.put( new RouteKey(method, path), handler);
    }
}
