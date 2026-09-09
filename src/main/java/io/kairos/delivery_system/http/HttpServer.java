package io.kairos.delivery_system.http;

import io.kairos.delivery_system.http.parser.RequestParser;
import io.kairos.delivery_system.http.request.HttpRequest;
import io.kairos.delivery_system.http.router.Router;
import io.kairos.delivery_system.http.router.RouterRegistry;
import io.kairos.delivery_system.http.serializer.HttpResponseSerializer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private final int port;
    private static volatile boolean running = true;

    public HttpServer(int port) {
        this.port = port;
    }

    public void start() {
        try (
                ServerSocket server = new ServerSocket(port);
                ExecutorService executor =
                        Executors.newVirtualThreadPerTaskExecutor()
        ) {

            // Graceful shutdown
            Runtime.getRuntime().addShutdownHook(
                    new Thread(() -> {
                        running = false;

                        try {
                            server.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    })
            );

            RouterRegistry.register();

            while (running) {
                try {
                    Socket socket = server.accept();

                    executor.execute(() -> {
                        try (socket) {

                            InputStream inputStream = socket.getInputStream();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                            HttpRequest request = RequestParser.parse(bufferedReader);

                            OutputStream outputStream = socket.getOutputStream();

                            byte[] response = HttpResponseSerializer.serialize(Router.route(request.method(), request.path(), request));
                            outputStream.write(response);
                            outputStream.flush();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                } catch (IOException e) {
                    if (running) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}