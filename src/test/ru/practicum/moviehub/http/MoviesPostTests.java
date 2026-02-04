package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.api.ErrorData;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoviesPostTests extends MoviesTestBaseClass{
    @Test
    void shouldPostMovies() throws Exception{

        String json =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1999\n" +
                        "}";
        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(201, resp.statusCode(), "POST /movies должен вернуть 201");

        String contentTypeHeaderValue =
                resp.headers().firstValue("Content-Type").orElse("");
        assertEquals("application/json; charset=UTF-8", contentTypeHeaderValue,
                "Content-Type должен содержать формат данных и кодировку");

        Movie movie = gson.fromJson(resp.body(), Movie.class);

        assertEquals("Matrix", movie.getTitle());
        assertEquals(1999, movie.getYear());
        assertTrue(movie.getId() > 0);
    }

    @Test
    void shouldHandleEmptyTitle() throws Exception{

        String json =
                "{\n" +
                        "  \"title\": \"\",\n" +
                        "  \"year\": 1999\n" +
                        "}";
        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(422, resp.statusCode(), "POST /movies с пустым заголовком должен вернуть 422");
    }

    @Test
    void shouldHandleTooLongTitle() throws Exception{

        String longTitle =
                "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "aaaaaaaaaa" +
                        "a";

        String json = String.format(
                "{\n" +
                        "  \"title\": \"%s\",\n" +
                        "  \"year\": 1999\n" +
                        "}",
                longTitle
        );

        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(422, resp.statusCode(), "POST /movies со слишком длинным заголовком должен вернуть 422");
    }

    @Test
    void shouldHandleIncorrectYearInPast() throws Exception{

        String json =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1777\n" +
                        "}";
        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(422, resp.statusCode(), "POST /movies с годом до 1888 должен вернуть 422");
    }

    @Test
    void shouldHandleIncorrectYearInFuture() throws Exception{

        String json =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 2028\n" +
                        "}";
        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(422, resp.statusCode(), "POST /movies с годом из будущего должен вернуть 422");
    }

    @Test
    void shouldHaveErrorDataWhenTitle422() throws Exception{

        String json =
                "{\n" +
                        "  \"title\": \"\",\n" +
                        "  \"year\": 1999\n" +
                        "}";
        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(422, resp.statusCode());

        ErrorData error = gson.fromJson(resp.body(), ErrorData.class);

        assertEquals("ошибка валидации", error.getError());
        assertEquals(1, error.getDetails().length);
    }

    @Test
    void shouldHaveErrorDataWhenYear422() throws Exception{

        String json =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1799\n" +
                        "}";
        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(422, resp.statusCode());
        ErrorData error = gson.fromJson(resp.body(), ErrorData.class);

        assertEquals("ошибка валидации", error.getError());
        assertEquals(1, error.getDetails().length);
    }

    @Test
    void shouldHandleSeveralErrors() throws Exception{

        String json =
                "{\n" +
                        "  \"title\": \"\",\n" +
                        "  \"year\": 1799\n" +
                        "}";
        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(422, resp.statusCode());

        ErrorData error = gson.fromJson(resp.body(), ErrorData.class);

        assertEquals("ошибка валидации", error.getError());
        assertEquals(2, error.getDetails().length,
                "При двух ошибках валидации details должен содержать 2 элемента");
    }

    @Test
    void shouldHandleSingleError() throws Exception{

        String json =
                "{\n" +
                        "  \"title\": \"\",\n" +
                        "  \"year\": 1999\n" +
                        "}";
        HttpRequest req1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp =
                client.send(req1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(422, resp.statusCode());

        ErrorData error = gson.fromJson(resp.body(), ErrorData.class);
        assertEquals("ошибка валидации", error.getError());
        assertEquals(1, error.getDetails().length,
                "При 1 ошибках валидации details должен содержать 1 элемента");
    }
}
