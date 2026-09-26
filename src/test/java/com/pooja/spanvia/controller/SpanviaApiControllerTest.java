package com.pooja.spanvia.controller;

import com.pooja.spanvia.exception.InvalidDatasetException;
import com.pooja.spanvia.service.SpanviaService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class SpanviaApiControllerTest {

    private SpanviaApiController controller;
    private SpanviaService service;

    @BeforeEach
    public void setUp() throws InvalidDatasetException {
        service = new SpanviaService();
        service.loadInitialData("data/heritage_sites.csv");
        controller = new SpanviaApiController(service);
    }

    @Test
    @DisplayName("Test GET /api/sites endpoint execution")
    public void testHandleGetAllSites() throws IOException {
        MockHttpExchange exchange = new MockHttpExchange("GET", "/api/sites");
        controller.handle(exchange);

        assertEquals(200, exchange.getResponseCode());
        assertTrue(exchange.getResponseBodyAsString().contains("Brihadeeswarar Temple"));
    }

    @Test
    @DisplayName("Test GET /api/sites/stats endpoint execution")
    public void testHandleGetStats() throws IOException {
        MockHttpExchange exchange = new MockHttpExchange("GET", "/api/sites/stats");
        controller.handle(exchange);

        assertEquals(200, exchange.getResponseCode());
        assertTrue(exchange.getResponseBodyAsString().contains("totalSites"));
    }

    @Test
    @DisplayName("Test GET /api/sites/festivals endpoint execution")
    public void testHandleGetFestivals() throws IOException {
        MockHttpExchange exchange = new MockHttpExchange("GET", "/api/sites/festivals");
        controller.handle(exchange);

        assertEquals(200, exchange.getResponseCode());
        assertTrue(exchange.getResponseBodyAsString().contains("featuredFestivals"));
    }

    @Test
    @DisplayName("Test GET /api/sites/unesco endpoint execution")
    public void testHandleGetUnesco() throws IOException {
        MockHttpExchange exchange = new MockHttpExchange("GET", "/api/sites/unesco");
        controller.handle(exchange);

        assertEquals(200, exchange.getResponseCode());
        assertTrue(exchange.getResponseBodyAsString().contains("Hampi"));
    }

    @Test
    @DisplayName("Test GET /api/sites/search with query param")
    public void testHandleSearch() throws IOException {
        MockHttpExchange exchange = new MockHttpExchange("GET", "/api/sites/search?keyword=Fort");
        controller.handle(exchange);

        assertEquals(200, exchange.getResponseCode());
        assertTrue(exchange.getResponseBodyAsString().contains("Fort"));
    }

    @Test
    @DisplayName("Test GET /api/sites/budget with query param")
    public void testHandleBudget() throws IOException {
        MockHttpExchange exchange = new MockHttpExchange("GET", "/api/sites/budget?max=2000");
        controller.handle(exchange);

        assertEquals(200, exchange.getResponseCode());
    }

    @Test
    @DisplayName("Test GET /api/sites/{id} valid and invalid ID")
    public void testHandleSiteById() throws IOException {
        MockHttpExchange validExchange = new MockHttpExchange("GET", "/api/sites/1");
        controller.handle(validExchange);
        assertEquals(200, validExchange.getResponseCode());

        MockHttpExchange invalidExchange = new MockHttpExchange("GET", "/api/sites/99999");
        controller.handle(invalidExchange);
        assertEquals(404, invalidExchange.getResponseCode());
    }

    @Test
    @DisplayName("Test POST /api/auth/login success and failure")
    public void testHandleLogin() throws IOException {
        MockHttpExchange validLogin = new MockHttpExchange("POST", "/api/auth/login", "{\"email\":\"test@spanvia.com\",\"password\":\"123456\"}");
        controller.handle(validLogin);
        assertEquals(200, validLogin.getResponseCode());

        MockHttpExchange invalidLogin = new MockHttpExchange("POST", "/api/auth/login", "{\"email\":\"\",\"password\":\"\"}");
        controller.handle(invalidLogin);
        assertEquals(400, invalidLogin.getResponseCode());
    }

    @Test
    @DisplayName("Test OPTIONS request handling for CORS")
    public void testHandleOptions() throws IOException {
        MockHttpExchange exchange = new MockHttpExchange("OPTIONS", "/api/sites");
        controller.handle(exchange);
        assertEquals(204, exchange.getResponseCode());
    }

    @Test
    @DisplayName("Test 404 for unknown route")
    public void testHandleUnknownRoute() throws IOException {
        MockHttpExchange exchange = new MockHttpExchange("GET", "/api/unknown/path");
        controller.handle(exchange);
        assertEquals(404, exchange.getResponseCode());
    }

    // Custom Mock for HttpExchange to facilitate unit testing without running a live server
    private static class MockHttpExchange extends HttpExchange {
        private String method;
        private URI uri;
        private Headers requestHeaders = new Headers();
        private Headers responseHeaders = new Headers();
        private ByteArrayOutputStream responseBody = new ByteArrayOutputStream();
        private ByteArrayInputStream requestBody;
        private int responseCode = -1;

        public MockHttpExchange(String method, String uriStr) {
            this(method, uriStr, "");
        }

        public MockHttpExchange(String method, String uriStr, String requestBodyStr) {
            this.method = method;
            this.uri = URI.create(uriStr);
            this.requestBody = new ByteArrayInputStream(requestBodyStr.getBytes());
        }

        @Override
        public Headers getRequestHeaders() { return requestHeaders; }

        @Override
        public Headers getResponseHeaders() { return responseHeaders; }

        @Override
        public URI getRequestURI() { return uri; }

        @Override
        public String getRequestMethod() { return method; }

        @Override
        public HttpContext getHttpContext() { return null; }

        @Override
        public void close() {}

        @Override
        public InputStream getRequestBody() { return requestBody; }

        @Override
        public OutputStream getResponseBody() { return responseBody; }

        @Override
        public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
            this.responseCode = rCode;
        }

        @Override
        public InetSocketAddress getRemoteAddress() { return null; }

        @Override
        public int getResponseCode() { return responseCode; }

        @Override
        public InetSocketAddress getLocalAddress() { return null; }

        @Override
        public String getProtocol() { return "HTTP/1.1"; }

        @Override
        public Object getAttribute(String name) { return null; }

        @Override
        public void setAttribute(String name, Object value) {}

        @Override
        public void setStreams(InputStream i, OutputStream o) {}

        @Override
        public HttpPrincipal getPrincipal() { return null; }

        public String getResponseBodyAsString() {
            return responseBody.toString();
        }
    }
}
