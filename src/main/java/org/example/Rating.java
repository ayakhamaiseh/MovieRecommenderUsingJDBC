package org.example;

public class Rating {
    private final int  userId;
    private final int movieId;
    private final int rating;

    public Rating(int userId, int movieId, int rating) {
        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
    }

    public int getUserId() {
        return userId;
    }
    public int getMovieId() {
        return movieId;
    }
    public int getRating() {
        return rating;
    }

}