package io.kairos.delivery_system.http.serializer;

import io.kairos.delivery_system.http.response.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpResponseSerializer {

    public static byte[] serialize(HttpResponse response) {

        byte[] body = serializeBody(response);

        String headers = serializeHeaders(response, body.length);

        byte[] headerBytes =
                headers.getBytes(StandardCharsets.US_ASCII);

        byte[] result =
                new byte[headerBytes.length + body.length];

        System.arraycopy(
                headerBytes,
                0,
                result,
                0,
                headerBytes.length
        );

        System.arraycopy(
                body,
                0,
                result,
                headerBytes.length,
                body.length
        );

        return result;
    }

    private static String serializeHeaders(
            HttpResponse response,
            int bodyLength
    ) {
        StringBuilder headers = new StringBuilder();

        headers.append("HTTP/1.1 ")
                .append(response.getStatusCode().getCode())
                .append(" ")
                .append(response.getStatusCode().getReasonPhrase())
                .append("\r\n");

        headers.append("Content-Type: application/json; charset=UTF-8\r\n");

        headers.append("Content-Length: ")
                .append(bodyLength)
                .append("\r\n");

        headers.append("Connection: close\r\n");

        if (response.getHeaders() != null) {
            for (Map.Entry<String, String> entry :
                    response.getHeaders().entrySet()) {

                headers.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\r\n");
            }
        }

        headers.append("\r\n");

        return headers.toString();
    }

    private static byte[] serializeBody(HttpResponse response) {

        StringBuilder json = new StringBuilder();

        json.append("{");

        boolean first = true;

        for (var entry : response.getBody().entrySet()) {

            if (!first) {
                json.append(",");
            }

            json.append("\"")
                    .append(escape(entry.getKey()))
                    .append("\":");

            json.append(serializeValue(entry.getValue()));

            first = false;
        }

        json.append("}");

        return json.toString()
                .getBytes(StandardCharsets.UTF_8);
    }

    private static String serializeValue(Object value) {

        if (value == null) {
            return "null";
        }

        if (value instanceof String) {
            return "\"" + escape((String) value) + "\"";
        }

        if (value instanceof Number || value instanceof Boolean) {
            return value.toString();
        }

        throw new IllegalArgumentException(
                "Unsupported JSON type: " + value.getClass()
        );
    }

    private static String escape(String value) {

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}