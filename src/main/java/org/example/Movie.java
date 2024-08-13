package org.example;

public class Movie {
    private final int movieId;
    private final String title;
    private final String genre;

    public Movie(int movieId, String title, String genre) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
    }

    public int getMovieId() {
        return movieId;
    }
    public String getTitle() {
        return title;
    }
    public String getGenre() {
        return genre;
    }


}