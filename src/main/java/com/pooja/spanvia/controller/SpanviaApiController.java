package com.pooja.spanvia.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.pooja.spanvia.exception.SiteNotFoundException;
import com.pooja.spanvia.model.HeritageSite;
import com.pooja.spanvia.service.SpanviaService;
import com.pooja.spanvia.util.JsonUtil;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class SpanviaApiController implements HttpHandler {

    private SpanviaService service;

    public SpanviaApiController(SpanviaService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String allowedOrigin = System.getenv().getOrDefault("FRONTEND_URL", "http://localhost:5173");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", allowedOrigin);
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");

        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQueryParams(query);

        try {

            if (path.equals("/api/sites") || path.equals("/api/sites/")) {
                handleGetAllSites(exchange);
            } else if (path.equals("/api/sites/filter")) {
                handleFilter(exchange, queryParams);
            } else if (path.equals("/api/sites/stats")) {
                handleGetStats(exchange);
            } else if (path.equals("/api/sites/festivals")) {
                handleGetFestivals(exchange);
            } else if (path.equals("/api/sites/unesco")) {
                handleGetUnesco(exchange);
            } else if (path.equals("/api/sites/search")) {
                handleSearch(exchange, queryParams);
            } else if (path.equals("/api/sites/budget")) {
                handleBudget(exchange, queryParams);
            } else if (path.startsWith("/api/sites/state/")) {
                String state = URLDecoder.decode(path.substring("/api/sites/state/".length()), "UTF-8");
                sendJsonResponse(exchange, 200, JsonUtil.toJsonList(service.filterByState(state)));
            } else if (path.equals("/api/sites/recommend") && "POST".equalsIgnoreCase(method)) {
                handleRecommend(exchange);
            } else if (path.equals("/api/chat") && "POST".equalsIgnoreCase(method)) {
                handleChat(exchange);
            } else if (path.equals("/api/auth/login") && "POST".equalsIgnoreCase(method)) {
                handleLogin(exchange);
            } else if (path.matches("/api/sites/\\d+")) {
                int id = Integer.parseInt(path.substring(path.lastIndexOf('/') + 1));
                HeritageSite site = service.getSiteByIdOrThrow(id);
                sendJsonResponse(exchange, 200, JsonUtil.toJson(site));
            } else {
                sendJsonResponse(exchange, 404, "{\"error\":\"Endpoint not found\"}");
            }
        } catch (SiteNotFoundException e) {
            sendJsonResponse(exchange, 404, "{\"error\":\"" + JsonUtil.escapeJson(e.getMessage()) + "\"}");
        } catch (Exception e) {
            e.printStackTrace();
            sendJsonResponse(exchange, 500, "{\"error\":\"Internal server error: " + JsonUtil.escapeJson(e.getMessage()) + "\"}");
        }

    }

    private void handleChat(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, String> bodyMap = parseSimpleJson(body);
        String message = bodyMap.getOrDefault("message", bodyMap.getOrDefault("prompt", ""));

        String answer = service.chatAI(message, new ArrayList<>());
        String jsonResponse = "{\"success\":true, \"answer\":\"" + JsonUtil.escapeJson(answer) + "\"}";
        sendJsonResponse(exchange, 200, jsonResponse);
    }

    private void handleFilter(HttpExchange exchange, Map<String, String> queryParams) throws IOException {
        String keyword = queryParams.getOrDefault("keyword", queryParams.getOrDefault("q", ""));
        String state = queryParams.getOrDefault("state", "All");
        String category = queryParams.getOrDefault("category", "All");
        double maxBudget = parseDouble(queryParams.get("maxBudget"), 0);
        double minRating = parseDouble(queryParams.get("minRating"), 0);
        Boolean unescoOnly = queryParams.containsKey("unescoOnly") ? Boolean.parseBoolean(queryParams.get("unescoOnly")) : false;
        String accessibility = queryParams.getOrDefault("accessibility", "All");
        String sortBy = queryParams.getOrDefault("sortBy", "rating-desc");

        List<HeritageSite> filtered = service.filterAndSortSites(keyword, state, category, maxBudget, minRating, unescoOnly, accessibility, sortBy);
        sendJsonResponse(exchange, 200, JsonUtil.toJsonList(filtered));
    }

    private void handleGetAllSites(HttpExchange exchange) throws IOException {

        List<HeritageSite> allSites = service.getAllSitesList();
        sendJsonResponse(exchange, 200, JsonUtil.toJsonList(allSites));
    }

    private void handleGetStats(HttpExchange exchange) throws IOException {
        List<HeritageSite> allSites = service.getAllSitesList();
        Set<String> states = new HashSet<>();
        int unescoCount = 0;
        for (HeritageSite s : allSites) {
            states.add(s.getState());
            if (s.isUnesco()) {
                unescoCount++;
            }
        }
        sendJsonResponse(exchange, 200, JsonUtil.toStatsJson(allSites.size(), unescoCount, states.size()));
    }

    private void handleGetFestivals(HttpExchange exchange) throws IOException {
        String[] festivals = service.getFeaturedFestivals();
        List<HeritageSite> sitesWithFestivals = service.getSitesWithFestivals();
        sendJsonResponse(exchange, 200, JsonUtil.toFestivalsJson(festivals, sitesWithFestivals));
    }

    private void handleGetUnesco(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, 200, JsonUtil.toJsonList(service.getUnescoSites()));
    }

    private void handleSearch(HttpExchange exchange, Map<String, String> queryParams) throws IOException {
        String keyword = queryParams.getOrDefault("keyword", queryParams.getOrDefault("q", ""));
        sendJsonResponse(exchange, 200, JsonUtil.toJsonList(service.filterByKeyword(keyword)));
    }

    private void handleBudget(HttpExchange exchange, Map<String, String> queryParams) throws IOException {
        double maxBudget = 5000;
        try {
            if (queryParams.containsKey("max")) {
                maxBudget = Double.parseDouble(queryParams.get("max"));
            }
        } catch (Exception ignored) {}
        sendJsonResponse(exchange, 200, JsonUtil.toJsonList(service.filterByBudget(maxBudget)));
    }

    private void handleRecommend(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, String> bodyMap = parseSimpleJson(body);

        String state = bodyMap.get("state");
        String category = bodyMap.get("category");
        double maxBudget = parseDouble(bodyMap.get("maxBudget"), 0);
        double minRating = parseDouble(bodyMap.get("minRating"), 0);
        String accessibility = bodyMap.get("accessibility");
        String travelSeason = bodyMap.get("travelSeason");
        String festival = bodyMap.get("festival");
        Boolean unesco = bodyMap.containsKey("unesco") ? Boolean.parseBoolean(bodyMap.get("unesco")) : null;

        List<HeritageSite> recommended = service.recommendAI(state, category, maxBudget, minRating, accessibility, travelSeason, festival, unesco);
        sendJsonResponse(exchange, 200, JsonUtil.toJsonList(recommended));
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        String body = readRequestBody(exchange);
        Map<String, String> bodyMap = parseSimpleJson(body);

        String email = bodyMap.getOrDefault("email", "");
        String password = bodyMap.getOrDefault("password", "");

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            sendJsonResponse(exchange, 400, "{\"success\":false, \"message\":\"Email and password are required\"}");
            return;
        }

        // Educational auth response
        String responseJson = "{" +
                "\"success\":true," +
                "\"message\":\"Login successful!\"," +
                "\"user\":{" +
                "\"name\":\"Heritage Enthusiast\"," +
                "\"email\":\"" + JsonUtil.escapeJson(email) + "\"," +
                "\"role\":\"Explorer\"" +
                "}" +
                "}";

        sendJsonResponse(exchange, 200, responseJson);
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return map;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            try {
                String key = URLDecoder.decode(kv[0], "UTF-8");
                String value = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
                map.put(key, value);
            } catch (Exception ignored) {}
        }
        return map;
    }

    private String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();
        if (json == null || json.trim().isEmpty()) return map;

        String content = json.trim();
        if (content.startsWith("{")) content = content.substring(1);
        if (content.endsWith("}")) content = content.substring(0, content.length() - 1);

        String[] tokens = content.split(",");
        for (String token : tokens) {
            String[] kv = token.split(":", 2);
            if (kv.length == 2) {
                String key = kv[0].trim().replaceAll("^\"|\"$", "");
                String value = kv[1].trim().replaceAll("^\"|\"$", "");
                map.put(key, value);
            }
        }
        return map;
    }

    private double parseDouble(String val, double defaultVal) {
        if (val == null || val.isEmpty()) return defaultVal;
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return defaultVal;
        }
    }

    private void sendJsonResponse(HttpExchange exchange, int statusCode, String responseJson) throws IOException {
        byte[] bytes = responseJson.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
