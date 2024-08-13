package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create instances of view, database manager, and recommender
        MovieView movieView = new MovieView();
        DataBaseManager dbManager = new DataBaseManager();
        MovieRecommender movieRecommender = new MovieRecommender(dbManager);
        MovieController movieController = new MovieController(movieView, dbManager);

        // Start the movie controller
        movieController.start();
    }
}
