package ru.practicum.moviehub.http;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.net.http.HttpClient;
import java.time.Duration;

public class MoviesTestBaseClass {
    protected static final String BASE = "http://localhost:8080";
    protected static MoviesServer server;
    protected static HttpClient client;
    Gson gson = new Gson();

    @BeforeAll
    static void beforeAll() {
        server = new MoviesServer();
        server.start();
        client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    @BeforeEach
    void beforeEach() {
        server.clearStore();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }
}
