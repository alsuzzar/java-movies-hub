package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoviesDeleteTests extends MoviesTestBaseClass{
    @Test
    void shouldReturn204WhenDeleteMovieById() throws Exception{
        String json =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        HttpRequest post = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> postResp =
                client.send(post, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(201, postResp.statusCode());

        HttpRequest delete = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1"))
                .DELETE()
                .build();

        HttpResponse<String> deleteResp =
                client.send(delete, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(204, deleteResp.statusCode(), "DELETE /movies/{id} должен вернуть 204");
    }

    @Test
    void shouldDeleteMovieById() throws Exception{
        String json =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        HttpRequest post = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> postResp =
                client.send(post, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(201, postResp.statusCode());

        HttpRequest delete = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1"))
                .DELETE()
                .build();

        HttpResponse<String> deleteResp =
                client.send(delete, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(204, deleteResp.statusCode());

        HttpRequest get = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1"))
                .GET()
                .build();

        HttpResponse<String> getResp =
                client.send(get, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(404, getResp.statusCode(), "GET /movies/{id} после удаления должен вернуть 404");
    }

    @Test
    void shouldHandleIncorrectIdForDelete() throws Exception{
        String json =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        HttpRequest post = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> postResp =
                client.send(post, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(201, postResp.statusCode());

        HttpRequest delete = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/21"))
                .DELETE()
                .build();

        HttpResponse<String> resp =
                client.send(delete, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(404, resp.statusCode(), "отсутствует фильм с указанным имдексом должен вернуть 404");
    }
}
