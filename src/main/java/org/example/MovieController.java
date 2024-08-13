package org.example;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MovieController {

    private final MovieView movieView;
    private final DataBaseManager dbManager;

    public MovieController(MovieView movieView, DataBaseManager dbManager) {
        this.movieView = movieView;
        this.dbManager = dbManager;
    }

    public void start() {
        int choice;
        do {
            movieView.displayMenu();
            choice = movieView.getUserChoice();
            switch (choice) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    addRating();
                    break;
                case 4:
                    getRecommendations();
                    break;
                case 5:
                    deleteMovie();
                    break;
                case 6:
                    deleteUser();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    break;
                default:
                    movieView.displayMessage("Invalid choice. Please try again.");
            }
        } while (choice != 7 );
    }

    private void addMovie() {
        Movie movie = movieView.getMovieDetailsFromUser();
        try {
            dbManager.connect();
            if(dbManager.movieExists(movie.getMovieId())){
                dbManager.updateMovies(movie);
                System.out.println("Movie updated successfully.");
            }
            else{
            dbManager.insertMovie(movie);
            System.out.println("Movie added successfully.");
            }
        } catch (SQLException e) {
            movieView.displayMessage("Error adding movie: " + e.getMessage());
        }
    }

    private void deleteMovie() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter movie ID to delete: ");
        int movieId = scanner.nextInt();

        try {
            dbManager.connect();
            if (dbManager.movieExists(movieId)) {
                // First delete associated ratings from ratings table
                dbManager.deleteRatingsByMovie(movieId);
                // Then delete the movie
                dbManager.deleteMovie(movieId);
                System.out.println("Movie deleted successfully.");
            } else {
                System.out.println("Movie ID does not exist.");
            }
        } catch (SQLException e) {
            movieView.displayMessage("Error deleting movie: " + e.getMessage());
        } finally {
            try {
                dbManager.disconnect();  // Ensure database connection is closed
            } catch (SQLException e) {
                movieView.displayMessage("Error closing database connection: " + e.getMessage());
            }
        }
    }




    private void addUser() {
        User user = movieView.getUserDetailsFromUser();
        try {
            dbManager.connect();
            if(dbManager.userExists(user.getId())){
                dbManager.updateUser(user);
                System.out.println("User updated successfully.");
            }
            else {
                dbManager.insertUser(user);
                System.out.println("User added successfully.");
            }
        } catch (SQLException e) {
            movieView.displayMessage("Error adding user: " + e.getMessage());
        }
    }

    private void deleteUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter user ID to delete: ");
        int userId = scanner.nextInt();  // Read user ID from input

        try {
            dbManager.connect();
            if (dbManager.userExists(userId)) {
                // First delete associated ratings from ratings table
                dbManager.deleteRatingsByUser(userId);
                // Then delete the user
                dbManager.deleteUser(userId);
                System.out.println("User deleted successfully.");
            } else {
                System.out.println("User ID does not exist.");
            }
        } catch (SQLException e) {
            movieView.displayMessage("Error deleting user: " + e.getMessage());
        } finally {
            try {
                dbManager.disconnect();  // Ensure database connection is closed
            } catch (SQLException e) {
                movieView.displayMessage("Error closing database connection: " + e.getMessage());
            }
        }
    }



    private void addRating() {
        Rating rating = movieView.getRatingDetailsFromUser();
        try {
            dbManager.connect();
            if(dbManager.ratingExists(rating.getUserId(), rating.getMovieId())) {
                dbManager.updateRating(rating);
                System.out.println("Rating updated successfully.");
            }
            else {
                dbManager.insertRating(rating);
                movieView.displayMessage("Rating added successfully.");
            }
        } catch (SQLException e) {
            movieView.displayMessage("Error adding rating: " + e.getMessage());
        }
    }
    private void getRecommendations() {
        int userId = movieView.getUserIdForRecommendations();
        try {
            List<Movie> recommendations = dbManager.getRecommendations(userId);
            movieView.displayRecommendations((ArrayList<Movie>) recommendations);
        } catch (SQLException e) {
            movieView.displayMessage("Error getting recommendations: " + e.getMessage());
        }
    }

}
