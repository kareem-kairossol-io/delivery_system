package io.kairos.delivery_system.http.request;

import java.util.Map;

public record HttpRequest(String method, String path, String version, Map<String, String> params,
                          Map<String, String> headers, Map<String, String> body) {

}