package ru.practicum.moviehub.api;

public class ErrorData {

    private final String error;
    private final String[] details;

    public ErrorData(String error, String[] details) {
        this.error = error;
        this.details = details;
    }

    public String getError() {
        return error;
    }

    public String[] getDetails() {
        return details;
    }
}
