package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoviesGetTests extends MoviesTestBaseClass{


    @Test
    void shouldGetMovieById() throws Exception{
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

        HttpRequest get = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1"))
                .GET()
                .build();

        HttpResponse<String> getResp =
                client.send(get, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(200, getResp.statusCode(), "GET /movies/{id} должен вернуть 200");

        Movie movie = gson.fromJson(getResp.body(), Movie.class);

        assertEquals(1, movie.getId());
        assertEquals("Matrix", movie.getTitle());
        assertEquals(1999, movie.getYear());
    }

    @Test
    void shouldHandleIfMovieByIdNotFound() throws Exception{

        HttpRequest get = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/1"))
                .GET()
                .build();

        HttpResponse<String> getResp =
                client.send(get, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(404, getResp.statusCode(), "GET /movies/{id} при отсутствии id должен вернуть 404");
    }

    @Test
    void shouldHandleIfIdNotInt() throws Exception{

        HttpRequest get = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/abc"))
                .GET()
                .build();

        HttpResponse<String> getResp =
                client.send(get, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(400, getResp.statusCode(), "GET /movies/{id} при id ≠ число должен вернуть 400");
    }

    @Test
    void shouldGetMoviesOfCertainYear() throws Exception{
        String json1 =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json2 =
                "{\n" +
                        "  \"title\": \"OtherFilm\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json3 =
                "{\n" +
                        "  \"title\": \"OtherFilm2\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json4 =
                "{\n" +
                        "  \"title\": \"OtherFilm3\",\n" +
                        "  \"year\": 1998\n" +
                        "}";

        HttpRequest post1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json1))
                .build();

        HttpRequest post2 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json2))
                .build();

        HttpRequest post3 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json3))
                .build();

        HttpRequest post4 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json4))
                .build();

        HttpResponse<String> postResp1 =
                client.send(post1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp2 =
                client.send(post2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp3 =
                client.send(post3, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp4 =
                client.send(post4, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));


        assertEquals(201, postResp1.statusCode());
        assertEquals(201, postResp2.statusCode());
        assertEquals(201, postResp3.statusCode());
        assertEquals(201, postResp4.statusCode());

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=1999"))
                .GET()
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(200, resp.statusCode(),
                "GET /movies?year=YYYY при корректном указании года должен вернуть 200");

        Movie[] movies = gson.fromJson(resp.body(), Movie[].class);
        assertEquals(3, movies.length);

        for (Movie movie : movies) {
            int year = movie.getYear();
            assertEquals(1999, year);
        }
    }

    @Test
    void shouldHandleIncorrectYear() throws Exception{
        String json1 =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json2 =
                "{\n" +
                        "  \"title\": \"OtherFilm\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json3 =
                "{\n" +
                        "  \"title\": \"OtherFilm2\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json4 =
                "{\n" +
                        "  \"title\": \"OtherFilm3\",\n" +
                        "  \"year\": 2001\n" +
                        "}";

        HttpRequest post1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json1))
                .build();

        HttpRequest post2 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json2))
                .build();

        HttpRequest post3 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json3))
                .build();

        HttpRequest post4 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json4))
                .build();

        HttpResponse<String> postResp1 =
                client.send(post1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp2 =
                client.send(post2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp3 =
                client.send(post3, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp4 =
                client.send(post4, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(201, postResp1.statusCode());
        assertEquals(201, postResp2.statusCode());
        assertEquals(201, postResp3.statusCode());
        assertEquals(201, postResp4.statusCode());

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=abc"))
                .GET()
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(400, resp.statusCode(),
                "GET /movies?year=YYYY при некорректном указании года должен вернуть 400");
    }

    @Test
    void shouldGetMoviesByYear_whenEmpty_returnsEmptyArray() throws Exception{
        String json1 =
                "{\n" +
                        "  \"title\": \"Matrix\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json2 =
                "{\n" +
                        "  \"title\": \"OtherFilm\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json3 =
                "{\n" +
                        "  \"title\": \"OtherFilm2\",\n" +
                        "  \"year\": 1999\n" +
                        "}";

        String json4 =
                "{\n" +
                        "  \"title\": \"OtherFilm3\",\n" +
                        "  \"year\": 2001\n" +
                        "}";

        HttpRequest post1 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json1))
                .build();

        HttpRequest post2 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json2))
                .build();

        HttpRequest post3 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json3))
                .build();

        HttpRequest post4 = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json4))
                .build();

        HttpResponse<String> postResp1 =
                client.send(post1, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp2 =
                client.send(post2, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp3 =
                client.send(post3, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        HttpResponse<String> postResp4 =
                client.send(post4, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(201, postResp1.statusCode());
        assertEquals(201, postResp2.statusCode());
        assertEquals(201, postResp3.statusCode());
        assertEquals(201, postResp4.statusCode());

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=1988"))
                .GET()
                .build();

        HttpResponse<String> resp =
                client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        Movie[] movies = gson.fromJson(resp.body(), Movie[].class);
        assertEquals(0, movies.length, "Ожидается JSON-массив");
    }
}
