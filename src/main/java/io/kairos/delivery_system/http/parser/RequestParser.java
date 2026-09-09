package io.kairos.delivery_system.http.parser;

import io.kairos.delivery_system.http.request.HttpRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    static public HttpRequest parse(BufferedReader bufferedReader) throws IOException {
        String requestLine = bufferedReader.readLine();

        String method = parseMethod(requestLine);
        String path =  parsePath(requestLine);
        String version = parseVersion(requestLine);

        Map<String, String> params = parseQueryParms(requestLine);
        Map<String, String> headers = parseHeader(bufferedReader);

        int bodyLength = headers.get("Content-Length") != null ? Integer.parseInt(headers.get("Content-Length")) : 0;
        Map<String, String> body = parseBody(bufferedReader, bodyLength);

        return new HttpRequest(method, path, version, params, headers, body);
    }

    private static String parseMethod(String requestLine) {
        return requestLine.split(" ")[0];
    }

    private static String parsePath(String requestLine) {
        String target = requestLine.split(" ")[1];

        return target.indexOf('?') != -1 ? target.substring(0, target.indexOf('?')) : target;
    }

    private static String parseVersion(String requestLine) {
        return requestLine.split(" ")[2];
    }

    private static Map<String, String> parseQueryParms(String requestLine) {
        Map<String, String> params = new HashMap<>();

        String target = requestLine.split(" ")[1];

        int index = target.indexOf('?');
        String paramsLine = index != -1 ? target.substring(index + 1) : "";

        String[] paramsArr = paramsLine.split("&");

        for (String param : paramsArr) {
            String[] keyValue = param.split("=", 2);
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }

        return params;
    }

    private static Map<String, String> parseHeader(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headers = new HashMap<>();

        String line;

        while ((line = bufferedReader.readLine()) != null && !line.isEmpty()) {

            int separator = line.indexOf(':');

            if (separator == -1) {
                continue;
            }

            String name = line.substring(0, separator).trim();
            String value = line.substring(separator + 1).trim();

            headers.put(name, value);
        }

        return headers;
    }

    private static Map<String, String> parseBody(BufferedReader bufferedReader, int contentLength) throws IOException {

        char[] bodyChars = new char[contentLength];

        int totalRead = 0;

        while (totalRead < contentLength) {

            int read = bufferedReader.read(
                    bodyChars,
                    totalRead,
                    contentLength - totalRead
            );

            if (read == -1) {
                throw new IOException("Unexpected end of request body");
            }

            totalRead += read;
        }

        return parseJsonBody(new String(bodyChars));
    }
    private static Map<String, String> parseJsonBody(String body) {
        Map<String, String> params = new HashMap<>();

        body = body.trim();

        if (body.isEmpty()) {
            return params;
        }

        body = body.substring(1, body.length() - 1).trim();

        if (body.isEmpty()) {
            return params;
        }

        String[] pairs = body.split(",");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);

            if (keyValue.length != 2) {
                continue;
            }

            String key = keyValue[0].trim();
            String value = keyValue[1].trim();

            key = key.replace("\"", "");
            value = value.replace("\"", "");

            params.put(key, value);
        }

        return params;
    }
}
