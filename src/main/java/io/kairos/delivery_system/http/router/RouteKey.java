package io.kairos.delivery_system.http.router;

public record RouteKey(
        String method,
        String path
) {}