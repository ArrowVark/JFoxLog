package io.github.arrowvark.jfoxlog.foxglove.servers.http;

import com.sun.net.httpserver.HttpServer;
import io.github.arrowvark.jfoxlog.foxglove.servers.Util;

import java.io.IOException;
import java.net.InetSocketAddress;

public class FoxgloveHttpServer {

    private static final FoxgloveHttpServer INSTANCE;

    private static HttpServer server;

    private static final int port = 5821;

    static {
        INSTANCE = new FoxgloveHttpServer(port);
        INSTANCE.start();
    }

    private FoxgloveHttpServer(int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FoxgloveHttpServer getInstance() {
        return INSTANCE;
    }

    private void start() {
        server.start();
    }

    public static String serveFile(String contextPath, String resourcePath) {
        String url = "http://" + Util.getLocalIp() + ":" + port + contextPath;

        server.createContext(contextPath, exchange -> {
            try (var stream = FoxgloveHttpServer.class.getResourceAsStream(resourcePath)) {
                if (stream == null) {
                    exchange.sendResponseHeaders(404, 0);
                    exchange.getResponseBody().close();
                    return;
                }
                byte[] bytes = stream.readAllBytes();
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.sendResponseHeaders(200, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.getResponseBody().close();
            }
        });
        return url;
    }
}
