package ru.practicum.moviehub.http;

import com.sun.net.httpserver.HttpHandler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

import com.google.gson.Gson;
import ru.practicum.moviehub.api.ErrorData;


abstract class BaseHttpHandler implements HttpHandler {
    protected static final String CT_JSON = "application/json; charset=UTF-8"; // !!! Укажите содержимое заголовка Content-Type
    Gson gson = new Gson();


    protected void sendJson(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", CT_JSON);
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    protected void sendErrorJson(HttpExchange ex, int status, String error, String[] details) throws IOException {
        ErrorData errorData = new ErrorData(error, details);
        String jsonErrorBody = gson.toJson(errorData);
        byte[] bytes = jsonErrorBody.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", CT_JSON);
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    protected void sendNoContent(HttpExchange ex) throws java.io.IOException {
        ex.getResponseHeaders().set("Content-Type", CT_JSON);
        ex.sendResponseHeaders(204, -1);
    }
}
