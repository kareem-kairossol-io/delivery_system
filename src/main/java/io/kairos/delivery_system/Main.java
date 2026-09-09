package io.kairos.delivery_system;

import io.kairos.delivery_system.http.HttpServer;

public class Main {
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);
        server.start();
    }
}