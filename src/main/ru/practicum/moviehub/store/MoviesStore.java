package ru.practicum.moviehub.store;
import ru.practicum.moviehub.model.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoviesStore {

    Map<Integer, Movie> moviesStore = new HashMap<>();
    private int idCount = 0;

    protected int generateId() {
        return ++idCount;
    }

    public ArrayList<Movie> getAllMovies() {
        return new ArrayList<>(moviesStore.values());
    }

    public Movie getMovieById(int id) {
        return moviesStore.get(id);
    }

    public void deleteMovieById(int id) {
        moviesStore.remove(id);
    }

    public void clearMovieStore() {
        moviesStore.clear();
        idCount = 0;
    }

    public Movie createMovie(String title, int year) {
        Movie movie = new Movie(generateId(), title, year);
        moviesStore.put(movie.getId(), movie);
        return movie;
    }
}
