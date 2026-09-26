package com.pooja.spanvia.app;

import com.sun.net.httpserver.HttpServer;
import com.pooja.spanvia.controller.SpanviaApiController;
import com.pooja.spanvia.service.SpanviaService;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class SpanviaServer {

    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            SpanviaService service = new SpanviaService();

            // Load initial 100 heritage sites dataset
            service.loadInitialData("data/heritage_sites.csv");

            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            SpanviaApiController controller = new SpanviaApiController(service);

            server.createContext("/api", controller);
            server.setExecutor(Executors.newFixedThreadPool(10));
            server.start();

            System.out.println("\n==================================================");
            System.out.println(" SPANVIA Heritage Tourism REST API Server Running");
            System.out.println(" URL: http://localhost:" + port + "/api/sites");
            System.out.println(" CORS Enabled for: http://localhost:5173");
            System.out.println("==================================================\n");

        } catch (Exception e) {
            System.err.println("Failed to start SPANVIA API server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
