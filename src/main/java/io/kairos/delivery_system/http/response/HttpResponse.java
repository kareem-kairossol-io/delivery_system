package io.kairos.delivery_system.http.response;

import io.kairos.delivery_system.http.enums.ResponseCodesEnum;

import java.util.HashMap;
import java.util.Map;
public class HttpResponse {

    private final ResponseCodesEnum statusCode;
    private final Map<String, String> headers;
    private final Map<String, Object> body;

    public HttpResponse(
            ResponseCodesEnum statusCode,
            Map<String, String> headers,
            Map<String, Object> body
    ) {
        this.statusCode = statusCode;
        this.headers = headers != null ? headers : new HashMap<>();
        this.body = body != null ? body : new HashMap<>();
    }

    public HttpResponse(ResponseCodesEnum statusCode) {
        this(statusCode, null, null);
    }

    public HttpResponse(
            ResponseCodesEnum statusCode,
            Map<String, Object> body
    ) {
        this(statusCode, null, body);
    }

    public HttpResponse(Map<String, Object> body) {
        this(ResponseCodesEnum.SUCCESS, null, body);
    }

    public ResponseCodesEnum getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, Object> getBody() {
        return body;
    }
}