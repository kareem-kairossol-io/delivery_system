package io.kairos.delivery_system.http.router;

import io.kairos.delivery_system.http.request.HttpRequest;
import io.kairos.delivery_system.http.response.HttpResponse;

import java.sql.SQLException;

@FunctionalInterface
public interface Handler {
    HttpResponse handle(HttpRequest request);
}
