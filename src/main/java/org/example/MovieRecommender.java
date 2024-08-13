package org.example;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieRecommender {
    private final DataBaseManager dbManager;

    public MovieRecommender(DataBaseManager dbManager) throws IOException {
        this.dbManager = new DataBaseManager();
    }

    public void addMovie(Movie movie) throws SQLException {
        dbManager.connect();
        if (dbManager.movieExists(movie.getMovieId())) {
            System.out.println("Movie already exists: " + movie.getTitle());
        } else {
            dbManager.insertMovie(movie);
            System.out.println("Added movie: " + movie.getTitle());
        }
        dbManager.disconnect();
    }

    public void addUser(User user) throws SQLException {
        dbManager.connect();
        if (dbManager.userExists(user.getId())) {
            System.out.println("User already exists: " + user.getUsername());
        } else {
            dbManager.insertUser(user);
            System.out.println("Added user: " + user.getUsername());
        }
        dbManager.disconnect();
    }

    public void addRating(Rating rating) throws SQLException {
        dbManager.connect();
        if (!dbManager.movieExists(rating.getMovieId())) {
            System.out.println("Movie doesn't exist to add a rating");
            return;
        }
        if (!dbManager.userExists(rating.getUserId())) {
            System.out.println("User doesn't exist, please add the user first");
            return;
        }
        if (dbManager.ratingExists(rating.getUserId(), rating.getMovieId())) {
            dbManager.updateRating(rating);
            System.out.println("Updated rating for user: " + rating.getUserId() + " movie: " + rating.getMovieId());
        } else {
            dbManager.insertRating(rating);
            System.out.println("Added rating for user: " + rating.getUserId() + " movie: " + rating.getMovieId());
        }
        dbManager.disconnect();
    }

    public ArrayList<Movie> getRecommendations(int userId) throws SQLException {
        dbManager.connect();
        Map<Integer, Double> movieRatings = new HashMap<>();
        Map<Integer, Integer> movieCounts = new HashMap<>();

        ArrayList<Rating> ratings = (ArrayList<Rating>) dbManager.getRatingsByUser(userId);
        for (Rating rating : ratings) {
            int movieId = rating.getMovieId();
            double ratingValue = rating.getRating();
            movieRatings.put(movieId, movieRatings.getOrDefault(movieId, 0.0) + ratingValue);
            movieCounts.put(movieId, movieCounts.getOrDefault(movieId, 0) + 1);
        }

        ArrayList<Movie> recommendations = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : movieRatings.entrySet()) {
            int movieId = entry.getKey();
            double totalRating = entry.getValue();
            int count = movieCounts.get(movieId);
            double averageRating = totalRating / count;

            if (averageRating >= 4.0) {
                Movie movie = dbManager.getMovie(movieId);
                if (movie != null) {
                    recommendations.add(movie);
                }
            }
        }
        dbManager.disconnect();
        return recommendations;
    }
}
